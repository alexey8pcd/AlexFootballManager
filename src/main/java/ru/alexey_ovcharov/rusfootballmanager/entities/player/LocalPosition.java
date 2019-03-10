package ru.alexey_ovcharov.rusfootballmanager.entities.player;

import ru.alexey_ovcharov.rusfootballmanager.common.FatalError;
import ru.alexey_ovcharov.rusfootballmanager.common.Randomization;

import javax.annotation.Nonnull;

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

    public static LocalPosition getByAbreviation(String abreviation) {
        LocalPosition[] values = LocalPosition.values();
        for (LocalPosition value : values) {
            if (value.abreviation.equalsIgnoreCase(abreviation)) {
                return value;
            }
        }
        return null;
    }

    private final String abreviation;
    private final double probability;

    private LocalPosition(String abreviation, double probability) {
        this.abreviation = abreviation;
        this.probability = probability;
    }

    @Nonnull
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
        throw new IllegalArgumentException();
    }

    public static LocalPosition generatePosition() {
        double chance = Randomization.nextDouble();
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
