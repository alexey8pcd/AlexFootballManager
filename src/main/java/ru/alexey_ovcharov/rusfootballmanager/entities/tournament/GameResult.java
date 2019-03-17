package ru.alexey_ovcharov.rusfootballmanager.entities.tournament;

public enum GameResult {
    WIN("В"),
    DRAW("Н"),
    LOSE("П");

    private final String shortName;

    GameResult(String shortName) {
        this.shortName = shortName;
    }

    public int getScore() {
        switch (this) {
            case WIN:
                return 3;
            case DRAW:
                return 1;
            default:
                return 0;
        }
    }

    public String getShortName() {
        return shortName;
    }
}
