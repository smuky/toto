package com.muky.toto.client;

import com.muky.toto.config.IsraelLeagueConfig;
import com.muky.toto.config.LeagueConfig;
import com.muky.toto.model.IsraelLeagueType;
import com.muky.toto.model.TeamScoreEntry;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class IsraelFootballAssociationClient extends IFAClientBase {

    private final IsraelLeagueConfig israelLeagueConfig;

    private static final String IFA_URL = "https://www.football.org.il/leagues/league/";

    public IsraelFootballAssociationClient(IsraelLeagueConfig israelLeagueConfig) {
        this.israelLeagueConfig = israelLeagueConfig;
    }
    //private static final String NATIONAL_LEAGUE_URL = "https://www.football.org.il/leagues/league/?league_id=45&season_id=27";

    @Cacheable(value = "league-type", key = "#leagueType + '-' + #seasonId")
    public List<TeamScoreEntry> getLigaScoreBoard(IsraelLeagueType leagueType, String seasonId) throws IOException {
        log.info("🔍 Cache MISS - Fetching league data for leagueType: " + leagueType + ", seasonId: " + seasonId);

        String leagueId = getLeagueId(leagueType);

        List<TeamScoreEntry> tableEntries = new ArrayList<>();
        WebDriver driver = getWebDriver(options);

        try {

            // Navigate to the page
            String url = IFA_URL + "?league_id=" + leagueId + "&season_id=" + seasonId;
            log.info("getLigaScoreBoard URL: {}", url);
            driver.get(url);

            // Wait a bit for JavaScript to load
            Thread.sleep(3000);

            // Get the page source and parse with JSoup
            String pageSource = driver.getPageSource();
            Document doc = Jsoup.parse(pageSource);

            // Find all table rows (using div elements with class "table_row")
            Elements tableRows = doc.select("a.table_row");

            if (tableRows.isEmpty()) {
                throw new IOException("Could not find any table rows");
            }

            for (Element row : tableRows) {
                try {
                    // Get all columns in this row
                    Elements cols = row.select("div.table_col");

                    if (cols.size() < 8) {
                        continue; // Skip rows with insufficient data
                    }

                    // Extract data based on the structure you provided:
                    // Col 0: Place (position)
                    // Col 1: Team name
                    // Col 2: Played (משחקים)
                    // Col 3: Won (ניצחונות)
                    // Col 4: Drawn (תיקו)
                    // Col 5: Lost (הפסדים)
                    // Col 6: Goals (שערים) - format: "12-29"
                    // Col 7: Points (נקודות)

                    String team = cols.get(1).text().replace("קבוצה", "").trim();
                    int played = parseIntSafely(cols.get(2).text().replace("משחקים", "").trim());
                    int won = parseIntSafely(cols.get(3).text().replace("ניצחונות", "").trim());
                    int drawn = parseIntSafely(cols.get(4).text().replace("תיקו", "").trim());
                    int lost = parseIntSafely(cols.get(5).text().replace("הפסדים", "").trim());

                    // Parse goals from format "12-29" (against-for)
                    String goalsText = cols.get(6).text().replace("שערים", "").trim();
                    int goalsFor = 0;
                    int goalsAgainst = 0;
                    int goalDifference = 0;

                    if (goalsText.contains("-")) {
                        String[] parts = goalsText.split("-");
                        if (parts.length == 2) {
                            goalsAgainst = parseIntSafely(parts[0].trim());
                            goalsFor = parseIntSafely(parts[1].trim());
                            goalDifference = goalsFor - goalsAgainst;
                        }
                    }

                    int points = parseIntSafely(cols.get(7).text().replace("נקודות", "").trim());

                    // Form data - not available in this format
                    String form = "";

                    TeamScoreEntry entry = new TeamScoreEntry(
                            team, leagueType.getLeagueEnum(), played, won, drawn, lost,
                            goalsFor, goalsAgainst, goalDifference,
                            points, form
                    );

                    tableEntries.add(entry);
                } catch (Exception e) {
                    // Log and continue with next row if parsing fails
                    log.error("Error parsing table row: " + e.getMessage());
                    e.printStackTrace();
                }
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Thread was interrupted while waiting for page to load", e);
        } catch (Exception e) {
            throw new IOException("Error fetching league table with Selenium: " + e.getMessage(), e);
        } finally {
            // Always close the browser
            if (driver != null) {
                driver.quit();
            }
        }

        return tableEntries;
    }

    private String getLeagueId(IsraelLeagueType leagueType) {
        LeagueConfig league = israelLeagueConfig.getLeagueByType(leagueType);
        String leagueId = String.valueOf(league.getLeagueId());
        return leagueId;
    }

    private int parseIntSafely(String value) {
        try {
            // Remove any non-numeric characters except minus sign
            String cleaned = value.replaceAll("[^0-9-]", "");
            return cleaned.isEmpty() ? 0 : Integer.parseInt(cleaned);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private WebDriver getWebDriver(ChromeOptions options) {
        // Initialize Chrome driver
        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        return driver;
    }
}
