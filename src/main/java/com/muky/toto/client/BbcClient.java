package com.muky.toto.client;

import com.muky.toto.model.EuropeLeagueType;
import com.muky.toto.model.TeamScoreEntry;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class BbcClient {

    private static final String BBC_BASE_URL = "https://www.bbc.com/sport/football/";
    public List<TeamScoreEntry> getLeagueScoreBoard(EuropeLeagueType leagueType) throws IOException {
        List<TeamScoreEntry> tableEntries = new ArrayList<>();

        String url = buildUrl(leagueType);
        String leagueName = extractLeagueNameFromUrl(leagueType.getBbcClientsuffix());
        
        // Fetch the page
        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                .timeout(10000)
                .get();

        // BBC uses dynamic CSS classes - find tables
        Elements tables = doc.select("table");
        if (tables.isEmpty()) {
            throw new IOException("No tables found on the page");
        }
        
        // If URL contains #<leagueName>, find the specific table by league name
        Element leagueTable = findLeagueTable(tables, leagueName);
        if (leagueTable == null) {
            throw new IOException("No matching table found on the page for league: " + leagueName);
        }
        Elements tableRows = leagueTable.select("tbody tr");

        for (Element row : tableRows) {
            try {
                Elements cells = row.select("td");
                
                if (cells.size() >= 9) {
                    // Extract team name from the team link
                    String team = extractTeamName(row);
                    
                    // Parse numeric values - BBC table structure:
                    // Cell 0: Position + Team name
                    // Cell 1: Played
                    // Cell 2: Won
                    // Cell 3: Drawn
                    // Cell 4: Lost
                    // Cell 5: Goals For
                    // Cell 6: Goals Against
                    // Cell 7: Goal Difference
                    // Cell 8: Points
                    // Cell 9: Form (if exists)
                    int played = parseIntSafely(cells.get(1).text());
                    int won = parseIntSafely(cells.get(2).text());
                    int drawn = parseIntSafely(cells.get(3).text());
                    int lost = parseIntSafely(cells.get(4).text());
                    int goalsFor = parseIntSafely(cells.get(5).text());
                    int goalsAgainst = parseIntSafely(cells.get(6).text());
                    int goalDifference = parseIntSafely(cells.get(7).text());
                    int points = parseIntSafely(cells.get(8).text());
                    
                    // Form is in the last cell (cell 9) - normalize it
                    String rawForm = cells.size() > 9 ? cells.get(9).text() : "";
                    String form = normalizeForm(rawForm);

                    TeamScoreEntry entry = new TeamScoreEntry(
                            team, leagueType.getLeagueEnum(), played, won, drawn, lost,
                            goalsFor, goalsAgainst, goalDifference,
                            points, form
                    );
                    
                    tableEntries.add(entry);
                }
            } catch (Exception e) {
                // Log and continue with next row if parsing fails
                log.error("Error parsing table row: ", e);
            }
        }

        return tableEntries;
    }

    private String buildUrl(EuropeLeagueType leagueType) {
        String suffix = leagueType.getBbcClientsuffix();
        // Remove the fragment part (#<leagueName>) from the URL as browsers don't send it to the server
        String urlSuffix = suffix.contains("#") ? suffix.substring(0, suffix.indexOf("#")) : suffix;
        return BBC_BASE_URL + urlSuffix;
    }

    private String extractLeagueNameFromUrl(String urlSuffix) {
        // Extract league name from URL fragment (e.g., "tables#Championship" -> "Championship")
        if (urlSuffix.contains("#")) {
            return urlSuffix.substring(urlSuffix.indexOf("#") + 1);
        }
        return null;
    }

    private Element findLeagueTable(Elements tables, String leagueName) {
        // If no league name specified (no # in URL), return the first table
        if (leagueName == null) {
            return tables.first();
        }
        
        // The page uses tabs with anchor links. Find the content associated with the tab.
        // Look for an element with id matching the league name (the anchor target)
        Element tabContent = tables.first().ownerDocument().getElementById(leagueName);
        if (tabContent != null) {
            // Find the table within or after this element
            Element table = tabContent.selectFirst("table");
            if (table != null) {
                log.debug("Found table for league '{}' by tab content id", leagueName);
                return table;
            }
        }
        // If no match found, log warning and return first table as fallback
        log.warn("Could not find table matching league name '{}', using first table as fallback", leagueName);
        return tables.first();
    }

    private String extractTeamName(Element row) {
        // BBC uses a specific link class for team names
        Element teamLink = row.select("a[class*=TeamNameLink]").first();
        if (teamLink != null) {
            String teamName = teamLink.text();
            log.debug("Extracted team name from TeamNameLink: {}", teamName);
            return teamName;
        }
        
        // Alternative: look for abbr tag which often contains team name
        Element abbr = row.select("abbr").first();
        if (abbr != null && !abbr.attr("title").isEmpty()) {
            String teamName = abbr.attr("title");
            log.debug("Extracted team name from abbr title: {}", teamName);
            return teamName;
        }
        
        // Alternative: look for any link in the first cell (position + team column)
        Elements cells = row.select("td");
        if (cells.size() > 0) {
            Element firstCell = cells.get(0);
            Element anyLink = firstCell.select("a").first();
            if (anyLink != null && !anyLink.text().isEmpty()) {
                String teamName = anyLink.text();
                log.debug("Extracted team name from first cell link: {}", teamName);
                return teamName;
            }
            
            // Last resort: extract from first cell text and remove position number
            String cellText = firstCell.text();
            // Remove leading digits and whitespace (e.g., "13 Bologna" -> "Bologna")
            String teamName = cellText.replaceFirst("^\\d+\\s+", "").trim();
            log.debug("Extracted team name from first cell text (removed position): {} -> {}", cellText, teamName);
            return teamName;
        }
        
        log.warn("Could not extract team name from row");
        return "";
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

    private String normalizeForm(String rawForm) {
        if (rawForm == null || rawForm.isEmpty()) {
            return "";
        }
        
        // Split by whitespace and extract only standalone W, L, and D (single-character words)
        String[] words = rawForm.split("\\s+");
        StringBuilder normalized = new StringBuilder();
        
        for (String word : words) {
            if (word.length() == 1 && (word.equals("W") || word.equals("L") || word.equals("D"))) {
                if (!normalized.isEmpty()) {
                    normalized.append(' ');
                }
                normalized.append(word);
            }
        }
        
        return normalized.toString();
    }
}
