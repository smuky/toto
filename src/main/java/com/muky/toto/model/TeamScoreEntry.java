package com.muky.toto.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
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

}
