package ru.alexey_ovcharov.rusfootballmanager.data;

import ru.alexey_ovcharov.rusfootballmanager.common.FatalError;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.LocalPosition;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static ru.alexey_ovcharov.rusfootballmanager.entities.player.LocalPosition.*;

public enum Tactics {

    T_4_4_2("4-4-2",
            GOALKEEPER,
            LEFT_DEFENDER, CENTRAL_DEFENDER, CENTRAL_DEFENDER, RIGHT_DEFENDER,
            LEFT_MIDFIELDER, CENTRAL_MIDFIELDER, CENTRAL_MIDFIELDER, RIGHT_MIDFIELDER,
            CENTRAL_FORWARD, CENTRAL_FORWARD),
    T_4_2_2_2("4-2-2-2", GOALKEEPER,
            LEFT_DEFENDER, CENTRAL_DEFENDER, CENTRAL_DEFENDER, RIGHT_DEFENDER,
            CENTRAL_MIDFIELDER, CENTRAL_MIDFIELDER, LEFT_MIDFIELDER, RIGHT_MIDFIELDER,
            CENTRAL_FORWARD, CENTRAL_FORWARD),
    T_4_1_4_1("4-1-4-1", GOALKEEPER,
            LEFT_DEFENDER, CENTRAL_DEFENDER, CENTRAL_DEFENDER, RIGHT_DEFENDER,
            CENTRAL_MIDFIELDER,
            LEFT_MIDFIELDER, ATTACK_MIDFIELDER, ATTACK_MIDFIELDER, RIGHT_MIDFIELDER,
            CENTRAL_FORWARD),
    T_4_1_2_1_2("4-1-2-1-2", GOALKEEPER,
            LEFT_DEFENDER, CENTRAL_DEFENDER, CENTRAL_DEFENDER, RIGHT_DEFENDER,
            CENTRAL_MIDFIELDER,
            LEFT_MIDFIELDER, RIGHT_MIDFIELDER,
            ATTACK_MIDFIELDER,
            CENTRAL_FORWARD, CENTRAL_FORWARD),
    T_4_3_3("4-3-3", GOALKEEPER,
            LEFT_DEFENDER, CENTRAL_DEFENDER, CENTRAL_DEFENDER, RIGHT_DEFENDER,
            LEFT_MIDFIELDER, CENTRAL_MIDFIELDER, RIGHT_MIDFIELDER,
            LEFT_WING_FORWARD, CENTRAL_FORWARD, RIGHT_WING_FORWARD),
    T_4_5_1("4-5-1", GOALKEEPER,
            LEFT_DEFENDER, CENTRAL_DEFENDER, CENTRAL_DEFENDER, RIGHT_DEFENDER,
            LEFT_MIDFIELDER, CENTRAL_MIDFIELDER, CENTRAL_MIDFIELDER, CENTRAL_MIDFIELDER, RIGHT_MIDFIELDER,
            CENTRAL_FORWARD),
    T_3_5_2("3-5-2", GOALKEEPER,
            CENTRAL_DEFENDER, CENTRAL_DEFENDER, CENTRAL_DEFENDER,
            LEFT_MIDFIELDER, CENTRAL_MIDFIELDER, CENTRAL_MIDFIELDER, CENTRAL_MIDFIELDER, RIGHT_MIDFIELDER,
            CENTRAL_FORWARD, CENTRAL_FORWARD),
    T_3_4_3("3-4-3", GOALKEEPER,
            CENTRAL_DEFENDER, CENTRAL_DEFENDER, CENTRAL_DEFENDER,
            LEFT_MIDFIELDER, CENTRAL_MIDFIELDER, CENTRAL_MIDFIELDER, RIGHT_MIDFIELDER,
            LEFT_WING_FORWARD, CENTRAL_FORWARD, RIGHT_WING_FORWARD),
    T_3_2_3_2("3-2-3-2", GOALKEEPER,
            CENTRAL_DEFENDER, CENTRAL_DEFENDER, CENTRAL_DEFENDER,
            CENTRAL_MIDFIELDER, CENTRAL_MIDFIELDER,
            LEFT_MIDFIELDER, ATTACK_MIDFIELDER, RIGHT_MIDFIELDER,
            CENTRAL_FORWARD, CENTRAL_FORWARD),
    T_5_4_1("5-4-1", GOALKEEPER,
            LEFT_DEFENDER, CENTRAL_DEFENDER, CENTRAL_DEFENDER, CENTRAL_DEFENDER, RIGHT_DEFENDER,
            LEFT_MIDFIELDER, CENTRAL_MIDFIELDER, CENTRAL_MIDFIELDER, RIGHT_MIDFIELDER,
            CENTRAL_FORWARD),
    T_5_3_2("5-3-2", GOALKEEPER,
            LEFT_DEFENDER, CENTRAL_DEFENDER, CENTRAL_DEFENDER, CENTRAL_DEFENDER, RIGHT_DEFENDER,
            LEFT_MIDFIELDER, CENTRAL_MIDFIELDER, RIGHT_MIDFIELDER,
            CENTRAL_FORWARD, CENTRAL_FORWARD),
    T_5_1_3_1("5-1-3-1", GOALKEEPER,
            LEFT_DEFENDER, CENTRAL_DEFENDER, CENTRAL_DEFENDER, CENTRAL_DEFENDER, RIGHT_DEFENDER,
            CENTRAL_MIDFIELDER,
            LEFT_MIDFIELDER, ATTACK_MIDFIELDER, RIGHT_MIDFIELDER,
            CENTRAL_FORWARD);

    private final String shortName;
    private final List<LocalPosition> positions;

    Tactics(String shortName, LocalPosition... positions) {
        this.shortName = shortName;
        this.positions = Collections.unmodifiableList(Arrays.asList(positions));
        if (positions.length != 11) {
            throw new FatalError("Players count not 11");
        }
    }

    @Nonnull
    public static Tactics getByShortName(@Nullable String shortName) {
        return Arrays.stream(values())
                     .filter(tactics -> tactics.shortName.equals(shortName))
                     .findFirst()
                     .orElseThrow(IllegalArgumentException::new);
    }

    public String getShortName() {
        return shortName;
    }

    public List<LocalPosition> getPositions() {
        return positions;
    }

}
