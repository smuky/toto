package com.muky.toto.client.api_football;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiFootballResponse<T> {
    private String get;
    private Parameters parameters;
    private List<String> errors;
    private int results;
    private Paging paging;
    private List<T> response;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Parameters {
        private String country;
        private String league;
        private String season;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Paging {
        private int current;
        private int total;
    }
}
