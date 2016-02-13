package rusfootballmanager.school;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import static rusfootballmanager.common.Constants.RANDOM;
import rusfootballmanager.common.NamesStore;
import rusfootballmanager.entities.LocalPosition;
import rusfootballmanager.entities.Player;
import rusfootballmanager.entities.TalentType;
import rusfootballmanager.simulation.PlayerProgressParams;

/**
 * @author Alexey
 */
public class YoungPlayerCreator {

    public static final int MIN_COMMON = 20;
    public static final int MAX_COMMON = 40;
    public static final int BETWEEN_COMMON = MAX_COMMON - MIN_COMMON;

    public static final int MIN_AGE = 16;
    public static final int MAX_AGE = 21;
    public static final int BETWEEN_AGE = MAX_AGE - MIN_AGE;

    public static Player createYoungPlayer() {
        int age = RANDOM.nextInt(BETWEEN_AGE) + MIN_AGE;
        TalentType talentType = TalentType.getByProbability();
        int averageByMinAge = RANDOM.nextInt(BETWEEN_COMMON) + MIN_COMMON;
        int average = averageByMinAge;
        if (age > MIN_AGE) {
            for (int i = 0; i < age - MIN_AGE; i++) {
                average += PlayerProgressParams.CONSTANTS.get(talentType).get(i);
            }
        }
        Player player = new Player(LocalPosition.generatePosition(), average, age,
                NamesStore.getInstance().getRandomFirstName(),
                NamesStore.getInstance().getRandomLastName());
        return player;
    }

}
