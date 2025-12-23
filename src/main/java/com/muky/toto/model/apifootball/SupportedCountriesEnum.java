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
    PORTUGAL("Portugal"),
    BELGIUM("Belgium"),
    AFRICA("World");

    private final String name;

    SupportedCountriesEnum(String name) {
        this.name = name;
    }
}
