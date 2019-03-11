package ru.alexey_ovcharov.rusfootballmanager.data;

import ru.alexey_ovcharov.rusfootballmanager.entities.player.LocalPosition;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public enum Tactics {

    T_4_4_2("4-4-2"),
    T_4_2_2_2("4-2-2-2"),
    T_4_1_4_1("4-1-4-1"),
    T_4_1_2_1_2("4-1-2-1-2"),
    T_4_3_3("4-3-3"),
    T_4_5_1("4-5-1"),
    T_3_5_2("3-5-2"),
    T_3_4_3("3-4-3"),
    T_3_2_3_2("3-2-3-2"),
    T_5_4_1("5-4-1"),
    T_5_3_2("5-3-2"),
    T_5_1_3_1("5-1-3-1");

    private String shortName;
    private List<LocalPosition> positions;

    Tactics(String shortName, LocalPosition... positions) {
        this.shortName = shortName;
        this.positions = Arrays.asList(positions);
    }
}
