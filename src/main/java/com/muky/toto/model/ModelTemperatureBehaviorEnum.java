package com.muky.toto.model;

import lombok.Getter;

@Getter
public enum ModelTemperatureBehaviorEnum {
    DETERMINISTIC(0.0, 0.1, "Fully deterministic, factual, minimal hallucinations"),
    BALANCED(0.2, 0.6, "Balanced, reasonable creativity, still mostly factual"),
    CREATIVE(0.7, 0.9, "Clearly creative, more imaginative, less accurate"),
    WILD(1.0, 1.3, "Wild, diverse, unpredictable, story-like generation"),
    CHAOTIC(1.4, 2.0, "Very chaotic, surreal, dreamlike — can invent anything");

    private final double minTemperature;
    private final double maxTemperature;
    private final String description;

    ModelTemperatureBehaviorEnum(double minTemperature, double maxTemperature, String description) {
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.description = description;
    }

    public double getMidTemperature() {
        return (minTemperature + maxTemperature) / 2.0;
    }

    public boolean isInRange(double temperature) {
        return temperature >= minTemperature && temperature <= maxTemperature;
    }

    public static ModelTemperatureBehaviorEnum fromTemperature(double temperature) {
        for (ModelTemperatureBehaviorEnum behavior : values()) {
            if (behavior.isInRange(temperature)) {
                return behavior;
            }
        }
        throw new IllegalArgumentException("Temperature " + temperature + " is out of valid range (0.0 - 2.0)");
    }
}
