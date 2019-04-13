package ru.alexey_ovcharov.rusfootballmanager.entities.sponsor;

public enum Status {
    LOW(125_000),
    LOW_MID(250_000),
    MID(375_000),
    HIGH_MID(500_000),
    HIGH(625_000),
    VERY_HIGH(750_000);
    private final long fareValue;

    private Status(long fareValue) {
        this.fareValue = fareValue;
    }

    public long getFareValue() {
        return fareValue;
    }

}
