package rusfootballmanager.entities;

/**
 * @author Alexey
 */
public class Contract {

    private int duration;
    private int fare;

    public Contract(int duration, int fare) {
        this.duration = duration;
        this.fare = fare;
    }

    public int getDuration() {
        return duration;
    }

    public void decreaseDiration() {
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
