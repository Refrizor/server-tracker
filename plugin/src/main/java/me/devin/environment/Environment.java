package me.devin.environment;

public enum Environment {
    PRODUCTION(0),
    DEVELOPMENT(1);

    private final int value;

    Environment(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}