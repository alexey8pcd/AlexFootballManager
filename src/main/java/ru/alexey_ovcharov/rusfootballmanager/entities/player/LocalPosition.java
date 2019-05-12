package ru.alexey_ovcharov.rusfootballmanager.entities.player;

import ru.alexey_ovcharov.rusfootballmanager.common.Randomization;

import javax.annotation.Nonnull;

public enum LocalPosition {

    GOALKEEPER("ВР", 0.0909, "Вратарь"),
    LEFT_DEFENDER("ЛЗ", 0.0909, "Левый защитник"),
    CENTRAL_DEFENDER("ЦЗ", 0.0909, "Центральный защитник"),
    RIGHT_DEFENDER("ПЗ", 0.0909, "Правый защитник"),
    LEFT_MIDFIELDER("ЛП", 0.0909, "Левый полузащитник"),
    CENTRAL_MIDFIELDER("ЦП", 0.0909, "Центральный полузащитник"),
    RIGHT_MIDFIELDER("ПП", 0.0909, "Правый полузащитник"),
    ATTACK_MIDFIELDER("АП", 0.0909, "Атакующий полузащитик"),
    RIGHT_WING_FORWARD("ПКФ", 0.0909, "Правый крайний нападающий"),
    CENTRAL_FORWARD("ЦФ", 0.0909, "Центральный нападающий"),
    LEFT_WING_FORWARD("ЛКФ", 0.0909, "Левый крайний нападающий");

    public static LocalPosition getByAbbreviation(String abbreviation) {
        LocalPosition[] values = LocalPosition.values();
        for (LocalPosition value : values) {
            if (value.abreviation.equalsIgnoreCase(abbreviation)) {
                return value;
            }
        }
        return null;
    }

    private final String abreviation;
    private final String description;
    private final double probability;

    private LocalPosition(String abreviation, double probability, String description) {
        this.abreviation = abreviation;
        this.probability = probability;
        this.description = description;
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

    public String getDescription() {
        return description;
    }
}
