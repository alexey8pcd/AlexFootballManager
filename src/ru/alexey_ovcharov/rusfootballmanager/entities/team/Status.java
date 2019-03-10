package ru.alexey_ovcharov.rusfootballmanager.entities.team;

public enum Status {

    HOST(1),
    GUEST(-1);

    private final int value;

    private Status(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
