package ru.alexey_ovcharov.rusfootballmanager.entities.tournament;

public enum GameResult {
    WIN,
    DRAW,
    LOSE;

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
}
