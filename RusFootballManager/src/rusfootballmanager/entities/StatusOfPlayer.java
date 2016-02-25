package rusfootballmanager.entities;

public enum StatusOfPlayer {

    READY("Готов"),
    INJURED("Травмирован"),
    DISQUALIFIED("Дисквалифицирован");
    private final String description;

    private StatusOfPlayer(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
