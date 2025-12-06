package com.muky.toto.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class TeamScoreEntry {
    @Schema(description = "Team name")
    private String team;

    @Schema(description = "League")
    private LeagueEnum leagueEnum;

    //exist only for israel teams from football.org.il
    @Schema(description = "Team Id")
    private String teamId;
    
    @Schema(description = "Number of games played")
    private byte played;
    
    @Schema(description = "Number of games won")
    private byte won;
    
    @Schema(description = "Number of games drawn")
    private byte drawn;
    
    @Schema(description = "Number of games lost")
    private byte lost;
    
    @Schema(description = "Goals scored by the team")
    private byte goalsFor;
    
    @Schema(description = "Goals conceded by the team")
    private byte goalsAgainst;
    
    @Schema(description = "Goal difference (goals for minus goals against)")
    private short goalDifference;
    
    @Schema(description = "Total points earned")
    private byte points;
    
    @JsonIgnore
    @Schema(hidden = true)
    private short formEncoded;
    
    @Schema(description = "Recent form from last 6 games. Format: space-separated W/L/D letters. " +
            "First letter is the oldest game, last letter is the most recent game. " +
            "Example: 'W W L D W L' means oldest game was a win, most recent was a loss")
    public String getForm() {
        return decodeForm(formEncoded);
    }

    public TeamScoreEntry(String team, LeagueEnum leagueEnum, String teamId, int played, int won, int drawn, int lost,
                          int goalsFor, int goalsAgainst, int goalDifference, int points, String form) {
        this.team = team != null ? team.intern() : null;
        this.leagueEnum = leagueEnum;
        this.teamId = teamId != null ? teamId.intern() : null;
        this.played = (byte) played;
        this.won = (byte) won;
        this.drawn = (byte) drawn;
        this.lost = (byte) lost;
        this.goalsFor = (byte) goalsFor;
        this.goalsAgainst = (byte) goalsAgainst;
        this.goalDifference = (short) goalDifference;
        this.points = (byte) points;
        this.formEncoded = encodeForm(form);
    }
    
    private static short encodeForm(String form) {
        if (form == null || form.isEmpty()) {
            return 0;
        }
        
        short encoded = 0;
        String[] results = form.split("\\s+");
        int position = 0;
        
        for (String result : results) {
            if (position >= 8) break;
            
            byte value = switch (result) {
                case "W" -> 0b00;
                case "D" -> 0b01;
                case "L" -> 0b10;
                default -> 0b11;
            };
            
            encoded |= (value << (position * 2));
            position++;
        }
        
        return encoded;
    }
    
    private static String decodeForm(short encoded) {
        if (encoded == 0) {
            return "";
        }
        
        StringBuilder form = new StringBuilder();
        
        for (int i = 0; i < 8; i++) {
            byte value = (byte) ((encoded >> (i * 2)) & 0b11);
            
            if (value == 0b11) break;
            
            if (!form.isEmpty()) {
                form.append(' ');
            }
            
            form.append(switch (value) {
                case 0b00 -> 'W';
                case 0b01 -> 'D';
                case 0b10 -> 'L';
                default -> '?';
            });
        }
        
        return form.toString();
    }

}
