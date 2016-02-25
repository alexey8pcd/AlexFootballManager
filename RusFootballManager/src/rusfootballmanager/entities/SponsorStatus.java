package rusfootballmanager.entities;

public enum SponsorStatus {
    LOW(25_000),
    LOW_MID(50_000),
    MID(100_000),
    HIGH_MID(200_000),
    HIGH(400_000),
    VERY_HIGH(1_000_000);
    private long fareValue;

    private SponsorStatus(long fareValue) {
        this.fareValue = fareValue;
    }

    public long getFareValue() {
        return fareValue;
    }

}
