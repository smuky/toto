package com.muky.toto.model;

import io.swagger.v3.oas.annotations.media.Schema;

public class TeamGamesEntry {
    @Schema(description = "Game date")
    private String date;
    
    @Schema(description = "Home team name")
    private String homeTeam;
    
    @Schema(description = "Away team name")
    private String awayTeam;
    
    @Schema(description = "Stadium name")
    private String stadium;
    
    @Schema(description = "Game hour/time")
    private String hour;
    
    @Schema(description = "Game result (e.g., '2-1', 'TBD' if not played yet)")
    private String result;

    public TeamGamesEntry() {
    }

    public TeamGamesEntry(String date, String homeTeam, String awayTeam, 
                          String stadium, String hour, String result) {
        this.date = date;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.stadium = stadium;
        this.hour = hour;
        this.result = result;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public String getStadium() {
        return stadium;
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "TeamGamesEntry{" +
                "date='" + date + '\'' +
                ", homeTeam='" + homeTeam + '\'' +
                ", awayTeam='" + awayTeam + '\'' +
                ", stadium='" + stadium + '\'' +
                ", hour='" + hour + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
