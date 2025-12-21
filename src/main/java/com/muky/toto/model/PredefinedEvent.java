package com.muky.toto.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PredefinedEvent {
    private String key;
    private String displayName;
}
