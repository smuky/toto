package com.muky.toto.model.apifootball;

import lombok.Getter;

@Getter
public enum SupportedCountriesEnum {
    ISRAEL("Israel"),
    ENGLAND("England"),
    SPAIN("Spain"),
    FRANCE("France"),
    ITALY("Italy"),
    GERMANY("Germany"),
    PORTUGAL("Portugal");

    private final String name;

    SupportedCountriesEnum(String name) {
        this.name = name;
    }
}
