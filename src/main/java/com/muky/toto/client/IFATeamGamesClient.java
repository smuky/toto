package com.muky.toto.client;

import com.muky.toto.model.TeamGamesEntry;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class IFATeamGamesClient extends IFAClientBase {

    private final WebDriverPool webDriverPool;
    private static final String URL = "https://www.football.org.il/team-details/team-games/";

    public IFATeamGamesClient(WebDriverPool webDriverPool) {
        this.webDriverPool = webDriverPool;
    }

    @Cacheable(value = "team-games", key = "#teamId + '-' + #seasonId")
    public List<TeamGamesEntry> getGameList(String teamId, String seasonId) throws IOException {
        String teamLastGamesUrl = buildUrl(teamId, seasonId);
        List<TeamGamesEntry> gameEntries = new ArrayList<>();
        WebDriver driver = webDriverPool.getDriver();

        try {
            // Navigate to the page
            driver.get(teamLastGamesUrl);

            // Wait for JavaScript to load
            Thread.sleep(3000);

            // Get the page source and parse with JSoup
            String pageSource = driver.getPageSource();
            Document doc = Jsoup.parse(pageSource);

            // Find all game rows - the structure is <a class="table_row link_url">
            Elements gameRows = doc.select("a.table_row.link_url");

            for (Element row : gameRows) {
                try {
                    // Extract game data from div.table_col elements
                    Elements cols = row.select("div.table_col.align_content");
                    
                    if (cols.size() < 4) {
                        continue; // Skip incomplete rows
                    }
                    
                    // Extract text after the sr-only span
                    String date = extractColumnText(cols.get(0));
                    String game = extractColumnText(cols.get(1));
                    
                    int hourIndex = 3;
                    int resultIndex = 4;
                    
                    String stadium = "";
                    if (cols.size() >= 5) {
                        Element stadiumCol = cols.get(2);
                        if (stadiumCol.hasClass("new-desktop-only")) {
                            stadium = extractColumnText(stadiumCol);
                            hourIndex = 3;
                            resultIndex = 4;
                        } else {
                            hourIndex = 2;
                            resultIndex = 3;
                        }
                    }
                    
                    String hour = cols.size() > hourIndex ? extractColumnText(cols.get(hourIndex)) : "";
                    String result = cols.size() > resultIndex ? extractColumnText(cols.get(resultIndex)) : "TBD";
                    
                    // Skip games with missing results
                    if (result.isEmpty() || result.equals("טרם נקבעה") || result.equals("TBD")) {
                        continue;
                    }
                    
                    // Parse game string to extract home and away teams
                    String[] teams = parseGameString(game);
                    String homeTeam = teams[0];
                    String awayTeam = teams[1];

                    // Skip empty rows
                    if (date.isEmpty() && homeTeam.isEmpty() && awayTeam.isEmpty()) {
                        continue;
                    }

                    TeamGamesEntry entry = new TeamGamesEntry(
                            date, homeTeam, awayTeam, stadium, hour, 
                            result.isEmpty() ? "TBD" : result
                    );

                    gameEntries.add(entry);
                } catch (Exception e) {
                    log.error("Error parsing game row: ", e);
                }
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Thread was interrupted while waiting for page to load", e);
        } catch (Exception e) {
            throw new IOException("Error fetching team games with Selenium: " + e.getMessage(), e);
        } finally {
            webDriverPool.releaseDriver();
        }

        return gameEntries;
    }

    private String buildUrl(String teamId, String seasonId) {
        return URL + "?team_id=" + teamId + "&season_id=" + seasonId;
    }
    
    /**
     * Extracts text from a column, removing the sr-only span text
     */
    private String extractColumnText(Element col) {
        // Clone the element to avoid modifying the original
        Element clone = col.clone();
        // Remove sr-only spans
        clone.select("span.sr-only").remove();
        return clone.text().trim();
    }
    
    /**
     * Parses game string like "מכבי הרצליה דיוויד יחזקאל - הפועל ע. עפולה"
     * into home and away teams
     */
    private String[] parseGameString(String game) {
        String[] teams = new String[2];
        
        // Split by " - " to separate home and away teams
        String[] parts = game.split(" - ");
        
        if (parts.length == 2) {
            teams[0] = parts[0].trim(); // Home team
            teams[1] = parts[1].trim(); // Away team
        } else {
            // Fallback if format is different
            teams[0] = game;
            teams[1] = "";
        }
        
        return teams;
    }
}
