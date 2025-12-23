package com.muky.toto.controllers.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WinnerGameResponse {
    private int rowNumber;
    private String league;
    private String teamA;
    private String teamB;
}
