package ru.alexey_ovcharov.rusfootballmanager.entities.player;

import java.awt.*;
import java.util.EnumSet;
import java.util.Set;

/**
 * @author Alexey Амплуа игрока: вратарь, защитник, полузащитник, нападающий
 */
public enum GlobalPosition {

    FORWARD(0.6, "НАП", 0, Color.RED),
    MIDFIELDER(0.9, "ПЗЩ", 1, Color.CYAN),
    DEFENDER(0.5, "ЗАЩ", 2, Color.GREEN),
    GOALKEEPER(0.1, "ВР", 3, Color.ORANGE);

    private final double fatigueCoefficient;
    private final String abreviation;
    private final int index;
    private final Color color;

    GlobalPosition(double fatigueCoefficient, String abreviation, int index, Color color) {
        this.fatigueCoefficient = fatigueCoefficient;
        this.abreviation = abreviation;
        this.index = index;
        this.color = color;
    }

    public double getFatigueCoefficient() {
        return fatigueCoefficient;
    }

    public String getAbreviation() {
        return abreviation;
    }

    public static GlobalPosition getByIndex(int index) {
        GlobalPosition[] values = GlobalPosition.values();
        for (GlobalPosition globalPosition : values) {
            if (globalPosition.index == index) {
                return globalPosition;
            }
        }
        return DEFENDER;
    }

    public Set<LocalPosition> getLocalPositions() {
        switch (this) {
            case DEFENDER:
                return EnumSet.of(LocalPosition.CENTRAL_DEFENDER,
                        LocalPosition.LEFT_DEFENDER, LocalPosition.RIGHT_DEFENDER);
            case MIDFIELDER:
                return EnumSet.of(LocalPosition.ATTACK_MIDFIELDER,
                        LocalPosition.CENTRAL_MIDFIELDER,
                        LocalPosition.LEFT_MIDFIELDER, LocalPosition.RIGHT_MIDFIELDER);
            case FORWARD:
                return EnumSet.of(LocalPosition.CENTRAL_FORWARD,
                        LocalPosition.RIGHT_WING_FORWARD,
                        LocalPosition.LEFT_WING_FORWARD);
            default:
                return EnumSet.of(LocalPosition.GOALKEEPER);
        }
    }

    public Color getColor() {
        return color;
    }

}
