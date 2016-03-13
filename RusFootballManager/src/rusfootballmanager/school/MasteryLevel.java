package rusfootballmanager.school;

/**
 * @author Alexey
 */
public enum MasteryLevel {
    LOW(30, 50),
    LOW_MID(40, 60),
    MID(50, 70),
    HIGH(60, 80),
    VERY_HIGH(70, 93);
    private int min;
    private int max;

    private MasteryLevel(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

}
