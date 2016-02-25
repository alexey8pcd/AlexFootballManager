package rusfootballmanager.entities;

import rusfootballmanager.common.Randomization;

public enum TalentType {

    USUAL(750, 0),
    CAPABLE(40, 1),//способный
    HIDDEN(30, 2),
    TALANTED(30, 3),
    HIDDEN_TALENT(40, 4),
    EARLY_DISCLOSURE(40, 5),
    LATE_DISCLOSURE(30, 6),
    SPECIAL_DISCLOSURE(30, 7),
    GIFTED(8, 8),
    GENIUS(2, 9);
    private final int chance;
    private final int index;

    private TalentType(int chance, int index) {
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
        int chance = Randomization.RANDOM.nextInt(1000);
        int probability = 0;
        for (TalentType type : values()) {
            probability += type.chance;
            if (chance < probability) {
                return type;
            }

        }
        return GENIUS;
    }

}
