package ru.alexey_ovcharov.rusfootballmanager.common.util;

import javax.annotation.Nonnull;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoundRobinScheduleGenerator {

    private RoundRobinScheduleGenerator() {

    }

    @Nonnull
    public static List<List<Map.Entry<Integer, Integer>>> roundRobinScheduling(int teamsCount, int loopsCount) {
        List<List<Map.Entry<Integer, Integer>>> result = new ArrayList<>();
        for (int loopIndex = 0; loopIndex < loopsCount; loopIndex++) {
            for (int startTeamIndex = 1; startTeamIndex <= teamsCount - 1; startTeamIndex++) {
                int[] array = createArray(teamsCount, startTeamIndex);

                int tourIndex = loopIndex * (teamsCount - 1) + startTeamIndex;
                List<Map.Entry<Integer, Integer>> tt = printTour(teamsCount, array, tourIndex);
                result.add(tt);
            }
        }
        return result;
    }

    @Nonnull
    private static int[] createArray(int teamsCount, int startTeamIndex) {
        int[] array2 = new int[teamsCount - 1];
        for (int i = 0; i < teamsCount - 1; i++) {
            int v = i + startTeamIndex;
            if (v >= teamsCount) {
                v = (v % teamsCount) + 1;
            }
            array2[i] = v;
        }
        return array2;
    }

    @Nonnull
    private static List<Map.Entry<Integer, Integer>> printTour(int teamsCount, int[] array, int tourIndex) {
        List<Map.Entry<Integer, Integer>> entries = new ArrayList<>();
        if (tourIndex % 2 == 0) {
            entries.add(new AbstractMap.SimpleEntry<>(array[0], teamsCount));
        } else {
            entries.add(new AbstractMap.SimpleEntry<>(teamsCount, array[0]));
        }
        for (int i = 1; i < teamsCount / 2; i++) {
            if (tourIndex % 2 == 0) {
                entries.add(new AbstractMap.SimpleEntry<>(array[i], array[array.length - i]));
            } else {
                entries.add(new AbstractMap.SimpleEntry<>(array[array.length - i], array[i]));
            }
        }
        return entries;
    }
}
