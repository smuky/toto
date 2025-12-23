package com.muky.toto.controllers.requests;

import lombok.Data;

import java.util.List;

@Data
public class WinnerGamesRequest {
    private List<GameData> games;

    @Data
    public static class GameData {
        private int gameType;
        private int drawNumber;
        private int drawId;
        private String closeDateTime;
        private int stake;
        private int drawMajorVersionNumber;
        private int drawMinorVersionNumber;
        private List<Row> rows;
    }

    @Data
    public static class Row {
        private int rowNumber;
        private String day;
        private String time;
        private String eventStartTime;
        private String league;
        private String teamA;
        private String teamB;
        private String period;
        private String betRadarId;
        private String status;
        private List<Object> odds;
    }
}
