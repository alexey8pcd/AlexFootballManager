package rusfootballmanager.entities;

public enum Condition {
    EQUALS("Соответствует", 0),
    NOT_EQUALS("Не соответствует", 1),
    MORE("Больше", 2),
    LESS("Меньше", 3),
    MORE_AND_LESS("Больше, чем и меньше, чем", 4);
    private final String description;
    private final int index;

    private Condition(String description, int index) {
        this.description = description;
        this.index = index;
    }

    public String getDescription() {
        return description;
    }

    public int getIndex() {
        return index;
    }

    public static Condition getByIndex(int index) {
        Condition[] values = Condition.values();
        for (Condition condition : values) {
            if (condition.index == index) {
                return condition;
            }
        }
        return Condition.NOT_EQUALS;
    }

}
