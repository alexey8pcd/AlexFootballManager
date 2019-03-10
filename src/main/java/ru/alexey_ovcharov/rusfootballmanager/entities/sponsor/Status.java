package ru.alexey_ovcharov.rusfootballmanager.entities.sponsor;

public enum Status {
    LOW(50_000),
    LOW_MID(100_000),
    MID(200_000),
    HIGH_MID(500_000),
    HIGH(1_000_000),
    VERY_HIGH(2_000_000);
    private long fareValue;

    private Status(long fareValue) {
        this.fareValue = fareValue;
    }

    public long getFareValue() {
        return fareValue;
    }

}
