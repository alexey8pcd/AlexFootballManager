package rusfootballmanager.simulation;

import rusfootballmanager.entities.TalentType;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Alexey
 */
public class PlayerfProgressParams {

    public static final Map<Integer, List<Integer>> CONSTANTS;
    public static final double[] EXPERIENCE_GAINED_BY_AGE = {
        0.105, 0.093, 0.083, 0.075, 0.068, 0.061, 0.056, 0.051, 0.047, 0.043, 0.040,
        0.037, 0.034, 0.032, 0.030, 0.028, 0.026, 0.025, 0.023, 0.022, 0.021
    };

    static {
        Map<Integer, List<Integer>> map = new HashMap<>();
        map.put(TalentType.GENIUS.getIndex(),
                Arrays.asList(8, 7, 6, 5, 4, 3, 2, 2, 2, 2, 1, 1, 0, 0, -1, 0, -1, 0, -1, 0));
        map.put(TalentType.GIFTED.getIndex(),
                Arrays.asList(4, 4, 4, 4, 4, 4, 3, 3, 2, 2, 1, 0, 0, 0, 0, 0, 0, -1, -2, -2));
        map.put(TalentType.HIDDEN_TALENT.getIndex(),
                Arrays.asList(1, 1, 1, 3, 4, 4, 4, 4, 3, 2, 0, 0, 0, 0, 0, -1, -1, -1, -1, -1));
        map.put(TalentType.LATE_DISCLOSURE.getIndex(),
                Arrays.asList(1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0));
        map.put(TalentType.SPECIAL_DISCLOSURE.getIndex(),
                Arrays.asList(1, 2, 3, 2, 1, 1, 0, 0, 0, 0, 1, 2, 3, 1, 0, 0, -1, -1, -1, -1));
        map.put(TalentType.EARLY_DISCLOSURE.getIndex(),
                Arrays.asList(3, 3, 3, 3, 3, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -2, -3, -3));
        map.put(TalentType.TALANTED.getIndex(),
                Arrays.asList(3, 3, 3, 3, 2, 2, 2, 2, 2, 3, 3, 0, 0, 0, 0, -1, -1, -1, -1, -1));
        map.put(TalentType.HIDDEN.getIndex(),
                Arrays.asList(1, 1, 1, 0, 0, 2, 2, 3, 3, 3, 2, 2, 0, 0, 0, 0, -4, -4, -7, -6));
        map.put(TalentType.CAPABLE.getIndex(),
                Arrays.asList(3, 3, 3, 3, -1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, -1, -1, -1, -1, -1));
        map.put(TalentType.USUAL.getIndex(),
                Arrays.asList(2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 0, 0, 0, 0, -1, -1, -1, -1, -1));
        CONSTANTS = Collections.unmodifiableMap(map);
    }

}
