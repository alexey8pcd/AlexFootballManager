package ru.alexey_ovcharov.rusfootballmanager.entities.player;

import javax.annotation.Nonnull;
import java.util.EnumSet;
import java.util.Set;

import static ru.alexey_ovcharov.rusfootballmanager.entities.player.Characteristic.*;

/**
 * @author Alexey
 */
public final class CharacteristicsBuilder {

    private CharacteristicsBuilder() {

    }

    private static final EnumSet<Characteristic> OF_PRIMARY = EnumSet.of(
            SHOT_ACCURACY, SHOT_POWER, SPEED, ACCELERATION, DRIBBLING, BALL_CONTROL, INTELLIGENCE, REACTION);
    private static final EnumSet<Characteristic> WF_PRIMARY = EnumSet.of(
            PASS_ACCURACY, BALL_CONTROL, DRIBBLING, SHOT_ACCURACY, SHOT_POWER, POSITION_CHOISE, SPEED, ACCELERATION);
    private static final EnumSet<Characteristic> AM_PRIMARY = EnumSet.of(
            INTELLIGENCE, PASS_ACCURACY, BALL_CONTROL, DRIBBLING, SHOT_ACCURACY, STAMINA);
    private static final EnumSet<Characteristic> CM_PRIMARY = EnumSet.of(
            TACKLE, BALANCE, PASS_ACCURACY, INTELLIGENCE, MENTAL_STABILITY, STAMINA, POSITION_CHOISE);
    private static final EnumSet<Characteristic> WM_PRIMARY = EnumSet.of(
            SPEED, PASS_ACCURACY, SHOT_ACCURACY, ACCELERATION, DRIBBLING, STAMINA);
    private static final EnumSet<Characteristic> CD_PRIMARY = EnumSet.of(
            TACKLE, BALANCE, MENTAL_STABILITY, POSITION_CHOISE, COMPOSURE, ACCELERATION);
    private static final EnumSet<Characteristic> WD_PRIMARY = EnumSet.of(
            BALANCE, SPEED, ACCELERATION, PASS_ACCURACY, STAMINA, TACKLE);
    private static final EnumSet<Characteristic> GK_PRIMARY = EnumSet.of(
            COMPOSURE, POSITION_CHOISE, REFLECTION, REACTION, MENTAL_STABILITY);

    private static final EnumSet<Characteristic> OF_SECONDARY = EnumSet.complementOf(OF_PRIMARY);
    private static final EnumSet<Characteristic> WF_SECONDARY = EnumSet.complementOf(WF_PRIMARY);
    private static final EnumSet<Characteristic> AM_SECONDARY = EnumSet.complementOf(AM_PRIMARY);
    private static final EnumSet<Characteristic> CM_SECONDARY = EnumSet.complementOf(CM_PRIMARY);
    private static final EnumSet<Characteristic> WM_SECONDARY = EnumSet.complementOf(WM_PRIMARY);
    private static final EnumSet<Characteristic> CD_SECONDARY = EnumSet.complementOf(CD_PRIMARY);
    private static final EnumSet<Characteristic> WD_SECONDARY = EnumSet.complementOf(WD_PRIMARY);
    private static final EnumSet<Characteristic> GK_SECONDARY = EnumSet.complementOf(GK_PRIMARY);

    public static Set<Characteristic> getPrimaryChars(@Nonnull LocalPosition positionType) {
        switch (positionType) {
            case GOALKEEPER:
                return GK_PRIMARY;
            case LEFT_DEFENDER:
            case RIGHT_DEFENDER:
                return WD_PRIMARY;
            case CENTRAL_DEFENDER:
                return CD_PRIMARY;
            case LEFT_MIDFIELDER:
            case RIGHT_MIDFIELDER:
                return WM_PRIMARY;
            case CENTRAL_MIDFIELDER:
                return CM_PRIMARY;
            case ATTACK_MIDFIELDER:
                return AM_PRIMARY;
            case RIGHT_WING_FORWARD:
            case LEFT_WING_FORWARD:
                return WF_PRIMARY;
            default:
                return OF_PRIMARY;
        }
    }

    public static Set<Characteristic> getSecondaryChars(@Nonnull LocalPosition positionType) {
        switch (positionType) {
            case GOALKEEPER:
                return GK_SECONDARY;
            case LEFT_DEFENDER:
            case RIGHT_DEFENDER:
                return WD_SECONDARY;
            case CENTRAL_DEFENDER:
                return CD_SECONDARY;
            case LEFT_MIDFIELDER:
            case RIGHT_MIDFIELDER:
                return WM_SECONDARY;
            case CENTRAL_MIDFIELDER:
                return CM_SECONDARY;
            case ATTACK_MIDFIELDER:
                return AM_SECONDARY;
            case RIGHT_WING_FORWARD:
            case LEFT_WING_FORWARD:
                return WF_SECONDARY;
            default:
                return OF_SECONDARY;
        }
    }

}
