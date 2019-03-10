package ru.alexey_ovcharov.rusfootballmanager.entities.team;

public enum Strategy {

    ATTACK(1),
    BALANCE(0),
    DEFENCE(-1);

    private final int value;

    private Strategy(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    
    

}
