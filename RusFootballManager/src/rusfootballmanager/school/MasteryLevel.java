package rusfootballmanager.school;

/**
 * @author Alexey
 */
public enum MasteryLevel {
    LOW(25, 45),
    LOW_MID(35, 55),
    MID(45, 65),
    HIGH_MID(55, 75),
    HIGH(65, 85),
    VERY_HIGH(75, 93);
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
