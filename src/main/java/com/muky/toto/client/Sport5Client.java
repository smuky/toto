package com.muky.toto.client;

import com.muky.toto.model.LeagueEnum;
import com.muky.toto.model.TeamScoreEntry;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class Sport5Client {

    private static final String SPORT5_LEAGUE_TABLE_URL = "https://www.sport5.co.il/Pages/LeagueTable.aspx?FolderID=44";
    public List<TeamScoreEntry> getLeagueTable(LeagueEnum leagueEnum) throws IOException {
        List<TeamScoreEntry> tableEntries = new ArrayList<>();

        // Fetch the page
        Document doc = Jsoup.connect(SPORT5_LEAGUE_TABLE_URL)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                .timeout(10000)
                .get();

        // Find the league table by looking for the table with the specific header structure
        Elements tables = doc.select("table");
        Element leagueTable = null;
        Element headerRow = null;
        
        for (Element table : tables) {
            Elements rows = table.select("tr");
            for (Element row : rows) {
                Elements headerCells = row.select("th");
                if (!headerCells.isEmpty()) {
                    String headerText = headerCells.text();
                    // Look for the specific header pattern with קבוצה, מש', נצ', etc.
                    if (headerText.contains("קבוצה") && headerText.contains("מש") && headerText.contains("נצ")) {
                        leagueTable = table;
                        headerRow = row;
                        break;
                    }
                }
            }
            if (leagueTable != null) {
                break;
            }
        }
        
        if (leagueTable == null || headerRow == null) {
            throw new IOException("Could not find league table with expected header structure");
        }
        
        // Get data rows
        Elements tableRows = leagueTable.select("tbody tr");
        if (tableRows.isEmpty()) {
            tableRows = leagueTable.select("tr");
        }

        for (Element row : tableRows) {
            try {
                Elements cells = row.select("td");
                
                // Skip header rows or rows with insufficient data
                if (cells.isEmpty() || cells.size() < 5) {
                    continue;
                }
                
                // Skip if this is actually a header row
                if (cells.text().contains("קבוצה")) {
                    continue;
                }

                // Extract values using direct cell indices
                // Cell 0: Position number
                // Cell 1: Team name (with link inside div)
                // Cell 2: Played
                // Cell 3: Won
                // Cell 4: Drawn
                // Cell 5: Lost
                // Cell 6: Goals ratio (e.g., "8-26")
                // Cell 7: Goal difference
                // Cell 8: Points
                
                String team = "";
                Element teamCell = cells.get(1);
                Element teamLink = teamCell.select("a").first();
                if (teamLink != null && !teamLink.text().isEmpty()) {
                    team = teamLink.text().trim();
                } else {
                    team = teamCell.text().trim();
                }
                
                int played = parseIntSafely(cells.get(2).text());
                int won = parseIntSafely(cells.get(3).text());
                int drawn = parseIntSafely(cells.get(4).text());
                int lost = parseIntSafely(cells.get(5).text());
                
                // Parse goals from "יחס" column (format: "8-26" means 8 against, 26 for)
                String goalsRatio = cells.get(6).text().trim();
                int goalsFor = 0;
                int goalsAgainst = 0;
                if (!goalsRatio.isEmpty() && goalsRatio.contains("-")) {
                    String[] parts = goalsRatio.split("-");
                    if (parts.length == 2) {
                        goalsAgainst = parseIntSafely(parts[0].trim());
                        goalsFor = parseIntSafely(parts[1].trim());
                    }
                }
                
                int goalDifference = parseIntSafely(cells.get(7).text());
                int points = parseIntSafely(cells.get(8).text());
                
                // Form data - may or may not be present
                String form = "";

                TeamScoreEntry entry = new TeamScoreEntry(
                        team, leagueEnum, null, played, won, drawn, lost,
                        goalsFor, goalsAgainst, goalDifference,
                        points, form
                );
                
                tableEntries.add(entry);
            } catch (Exception e) {
                // Log and continue with next row if parsing fails
                System.err.println("Error parsing table row: " + e.getMessage());
                e.printStackTrace();
            }
        }

        return tableEntries;
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
                if (normalized.length() > 0) {
                    normalized.append(' ');
                }
                normalized.append(word);
            }
        }
        
        return normalized.toString();
    }
}
