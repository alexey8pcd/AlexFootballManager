package rusfootballmanager.entities;

import rusfootballmanager.common.Constants;

public enum LocalPosition {

    GOALKEEPER("ВР", 0.0909),
    LEFT_DEFENDER("ЛЗ", 0.0909),
    CENTRAL_DEFENDER("ЦЗ", 0.0909),
    RIGHT_DEFENDER("ПЗ", 0.0909),
    LEFT_MIDFIELDER("ЛП", 0.0909),
    CENTRAL_MIDFIELDER("ЦП", 0.0909),
    RIGHT_MIDFIELDER("ПП", 0.0909),
    ATTACK_MIDFIELDER("АП", 0.0909),
    RIGHT_WING_FORWARD("ПКФ", 0.0909),
    CENTRAL_FORWARD("ЦФ", 0.0909),
    LEFT_WING_FORWARD("ЛКФ", 0.0909);

    private final String abreviation;
    private final double probability;

    private LocalPosition(String abreviation, double probability) {
        this.abreviation = abreviation;
        this.probability = probability;
    }

    public GlobalPosition getPositionOnField() {
        switch (this) {
            case GOALKEEPER:
                return GlobalPosition.GOALKEEPER;
            case LEFT_DEFENDER:
            case CENTRAL_DEFENDER:
            case RIGHT_DEFENDER:
                return GlobalPosition.DEFENDER;
            case LEFT_MIDFIELDER:
            case CENTRAL_MIDFIELDER:
            case RIGHT_MIDFIELDER:
            case ATTACK_MIDFIELDER:
                return GlobalPosition.MIDFIELDER;
            case CENTRAL_FORWARD:
            case LEFT_WING_FORWARD:
            case RIGHT_WING_FORWARD:
                return GlobalPosition.FORWARD;
        }
        return null;
    }

    public static LocalPosition generatePosition() {
        double chance = Constants.RANDOM.nextDouble();
        double probability = 0;
        LocalPosition[] values = LocalPosition.values();
        for (LocalPosition value : values) {
            probability += value.probability;
            if (chance < probability) {
                return value;
            }
        }
        return RIGHT_WING_FORWARD;
    }

    public String getAbreviation() {
        return abreviation;
    }

}
