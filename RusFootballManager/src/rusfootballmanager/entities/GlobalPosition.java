package rusfootballmanager.entities;

import java.util.EnumSet;

/**
 * @author Alexey Амплуа игрока: вратарь, защитник, полузащитник, нападающий
 */
public enum GlobalPosition {

    FORWARD(0.6, "НАП"),
    MIDFIELDER(0.9, "ПЗЩ"),
    DEFENDER(0.5, "ЗАЩ"),
    GOALKEEPER(0.1, "ВР");

    private final double fatigueCoefficient;
    private final String abreviation;

    private GlobalPosition(double fatigueCoefficient, String abreviation) {
        this.fatigueCoefficient = fatigueCoefficient;
        this.abreviation = abreviation;
    }

    public double getFatigueCoefficient() {
        return fatigueCoefficient;
    }

    public String getAbreviation() {
        return abreviation;
    }

    public EnumSet<LocalPosition> getLocalPositions() {
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

}
