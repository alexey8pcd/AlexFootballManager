package ru.alexey_ovcharov.rusfootballmanager.entities.player;

/**
 * @author Alexey
 */
public class Contract {

    public static final int MIN_CONTRACT_DURATION = 1;
    public static final int DEFAULT_CONTRACT_DURATION = 3;
    public static final int MAX_CONTRACT_DURATION = 5;
    private int duration;
    private int fare;

    public Contract(int duration, int fare) {
        this.duration = duration;
        this.fare = fare;
    }

    public int getDuration() {
        return duration;
    }

    public void decreaseDuration() {
        if (duration > 0) {
            --duration;
        }
    }

    public void increaseDuration(int value) {
        duration += value;
    }

    public int getFare() {
        return fare;
    }

    public void setFare(int fare) {
        this.fare = fare;
    }

    @Override
    public String toString() {
        return "Длительность: " + duration + ", Зарплата: " + fare;
    }

}
