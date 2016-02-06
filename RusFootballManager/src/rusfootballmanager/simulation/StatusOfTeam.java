package rusfootballmanager.simulation;

public enum StatusOfTeam {

    HOST(1),
    GUEST(-1);

    private final int value;

    private StatusOfTeam(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
