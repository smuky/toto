package com.muky.toto.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class TeamScoreEntry {
    private String team;
    private LeagueEnum leagueEnum;
    private String teamId;
    private byte played;
    private byte won;
    private byte drawn;
    private byte lost;
    private byte goalsFor;
    private byte goalsAgainst;
    private short goalDifference;
    private byte points;
    
    @JsonIgnore
    private short formEncoded;
    
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
