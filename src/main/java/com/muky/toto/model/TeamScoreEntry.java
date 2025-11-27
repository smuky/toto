package com.muky.toto.model;

import io.swagger.v3.oas.annotations.media.Schema;

public class TeamScoreEntry {
    @Schema(description = "Team name")
    private String team;
    
    @Schema(description = "Number of games played")
    private int played;
    
    @Schema(description = "Number of games won")
    private int won;
    
    @Schema(description = "Number of games drawn")
    private int drawn;
    
    @Schema(description = "Number of games lost")
    private int lost;
    
    @Schema(description = "Goals scored by the team")
    private int goalsFor;
    
    @Schema(description = "Goals conceded by the team")
    private int goalsAgainst;
    
    @Schema(description = "Goal difference (goals for minus goals against)")
    private int goalDifference;
    
    @Schema(description = "Total points earned")
    private int points;
    
    @Schema(description = "Recent form from last 6 games. Format: space-separated W/L/D letters. " +
            "First letter is the oldest game, last letter is the most recent game. " +
            "Example: 'W W L D W L' means oldest game was a win, most recent was a loss")
    private String form;

    public TeamScoreEntry() {
    }

    public TeamScoreEntry(String team, int played, int won, int drawn, int lost,
                          int goalsFor, int goalsAgainst, int goalDifference,
                          int points, String form) {
        this.team = team;
        this.played = played;
        this.won = won;
        this.drawn = drawn;
        this.lost = lost;
        this.goalsFor = goalsFor;
        this.goalsAgainst = goalsAgainst;
        this.goalDifference = goalDifference;
        this.points = points;
        this.form = form;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public int getPlayed() {
        return played;
    }

    public void setPlayed(int played) {
        this.played = played;
    }

    public int getWon() {
        return won;
    }

    public void setWon(int won) {
        this.won = won;
    }

    public int getDrawn() {
        return drawn;
    }

    public void setDrawn(int drawn) {
        this.drawn = drawn;
    }

    public int getLost() {
        return lost;
    }

    public void setLost(int lost) {
        this.lost = lost;
    }

    public int getGoalsFor() {
        return goalsFor;
    }

    public void setGoalsFor(int goalsFor) {
        this.goalsFor = goalsFor;
    }

    public int getGoalsAgainst() {
        return goalsAgainst;
    }

    public void setGoalsAgainst(int goalsAgainst) {
        this.goalsAgainst = goalsAgainst;
    }

    public int getGoalDifference() {
        return goalDifference;
    }

    public void setGoalDifference(int goalDifference) {
        this.goalDifference = goalDifference;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    @Override
    public String toString() {
        return "TeamTableEntry{" +
                "team='" + team + '\'' +
                ", played=" + played +
                ", won=" + won +
                ", drawn=" + drawn +
                ", lost=" + lost +
                ", goalsFor=" + goalsFor +
                ", goalsAgainst=" + goalsAgainst +
                ", goalDifference=" + goalDifference +
                ", points=" + points +
                ", form='" + form + '\'' +
                '}';
    }
}
