package com.muky.toto.model;

import lombok.Getter;

@Getter
public enum SupportedLanguageEnum {
    EN("english"),
    ES("spanish"),
    IT("italian"),
    DE("german"),
    FR("french"),
    HE("hebrew");

    private final String language;

    SupportedLanguageEnum(String language) {
        this.language = language;
    }
}
