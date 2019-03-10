package ru.alexey_ovcharov.rusfootballmanager.entities.player;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * @author Alexey
 */
public class ProgressParameters {

    
    public static final Map<TalentType, List<Integer>> CONSTANTS;
    public static final double[] EXPERIENCE_GAINED_BY_AGE = {
        0.105, 0.093, 0.083, 0.075, 0.068, 0.061, 0.056, 0.051, 0.047, 0.043, 0.040,
        0.037, 0.034, 0.032, 0.030, 0.028, 0.026, 0.025, 0.023, 0.022, 0.021
    };

    static {
        CONSTANTS = new EnumMap<>(TalentType.class);
        CONSTANTS.put(TalentType.GENIUS,
                            //17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36
                Arrays.asList(8, 7, 6, 5, 4, 3, 2, 2, 2, 2, 1, 1, 0, 0, -1, 0, -1, 0, -1, 0));
        CONSTANTS.put(TalentType.GIFTED,
                Arrays.asList(4, 4, 4, 4, 4, 4, 3, 3, 2, 2, 1, 0, 0, 0, 0, 0, 0, -1, -2, -2));
        CONSTANTS.put(TalentType.HIDDEN_TALENT,
                Arrays.asList(1, 1, 1, 3, 4, 4, 4, 4, 3, 2, 0, 0, 0, 0, 0, -1, -1, -1, -1, -1));
        CONSTANTS.put(TalentType.LATE_DISCLOSURE,
                Arrays.asList(1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0));
        CONSTANTS.put(TalentType.SPECIAL_DISCLOSURE,
                Arrays.asList(1, 2, 3, 2, 1, 1, 0, 0, 0, 0, 1, 2, 3, 1, 0, 0, -1, -1, -1, -1));
        CONSTANTS.put(TalentType.EARLY_DISCLOSURE,
                Arrays.asList(3, 3, 3, 3, 3, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -2, -3, -3));
        CONSTANTS.put(TalentType.TALANTED,
                Arrays.asList(3, 3, 3, 3, 2, 2, 2, 2, 2, 3, 3, 0, 0, 0, 0, -1, -1, -1, -1, -1));
        CONSTANTS.put(TalentType.HIDDEN,
                Arrays.asList(1, 1, 1, 0, 0, 2, 2, 3, 3, 3, 2, 2, 0, 0, 0, 0, -4, -4, -7, -6));
        CONSTANTS.put(TalentType.CAPABLE,
                Arrays.asList(3, 3, 3, 3, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, -1, -1, -1, -1, -1));
        CONSTANTS.put(TalentType.USUAL,
                Arrays.asList(2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 0, 0, 0, 0, -1, -1, -1, -1, -1));
    }

}
