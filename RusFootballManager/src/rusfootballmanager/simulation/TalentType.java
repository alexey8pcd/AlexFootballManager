package rusfootballmanager.simulation;

public enum TalentType {

    USUAL(75, 0),
    CAPABLE(4, 1),//способный
    HIDDEN(3, 2),
    TALANTED(3, 3),
    HIDDEN_TALENT(4, 4),
    EARLY_DISCLOSURE(4, 5),
    LATE_DISCLOSURE(3, 6),
    SPECIAL_DISCLOSURE(3, 7),
    GIFTED(0.8, 8),
    GENIUS(0.2, 9);
    private final double chance;
    private final int index;

    private TalentType(double chance, int index) {
        this.chance = chance;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public double getChance() {
        return chance;
    }

    public static TalentType getByProbability() {
        int chance = Common.RANDOM.nextInt(10000);
        double probability;
        for (TalentType type : values()) {
            probability = type.chance;
            if (chance < probability * 100) {
                return type;
            }
            probability += type.chance;
        }
        return USUAL;
    }

}
