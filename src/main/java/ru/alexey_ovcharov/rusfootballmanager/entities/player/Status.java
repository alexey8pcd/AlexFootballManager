package ru.alexey_ovcharov.rusfootballmanager.entities.player;

/**
 * Статус игрока
 */
public enum Status {

    READY("Готов"),
    INJURED("Травмирован"),
    DISQUALIFIED("Дисквалифицирован");
    private final String description;

    private Status(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
