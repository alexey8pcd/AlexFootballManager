package rusfootballmanager.school;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import static rusfootballmanager.common.Randomization.RANDOM;
import rusfootballmanager.common.NamesStore;
import rusfootballmanager.entities.GlobalPosition;
import rusfootballmanager.entities.LocalPosition;
import rusfootballmanager.entities.Player;
import static rusfootballmanager.entities.Player.MAX_AGE;
import static rusfootballmanager.entities.Player.MAX_YOUNG_AGE;
import static rusfootballmanager.entities.Player.MIN_AGE;
import rusfootballmanager.entities.TalentType;
import rusfootballmanager.simulation.PlayerProgressParams;

/**
 * @author Alexey
 */
public class PlayerCreator {

    public static final int MIN_COMMON = 20;
    public static final int MAX_YOUNG_COMMON = 50;

    public static final int MID_YOUNG_COMMON = (MAX_YOUNG_COMMON + MIN_COMMON) / 2;
    public static final int BETWEEN_YOUNG_COMMON = MAX_YOUNG_COMMON - MIN_COMMON;

    
    
    public static final int BETWEEN_YOUNG_AGE = MAX_YOUNG_AGE - MIN_AGE;
    public static final int BETWEEN_AGE = MAX_AGE - MAX_YOUNG_AGE;

    public static Player createYoungPlayerSimple() {
        return createYoungPlayer(0);
    }

    /**
     * Создает случайного молодого игрока в возрасте от 16 до 21 года. Позиция
     * может быть любая.
     *
     * @param sportschoolLevel from 0 to 10
     * @return
     */
    public static Player createYoungPlayer(int sportschoolLevel) {
        if (sportschoolLevel < 0 || sportschoolLevel > 10) {
            throw new IllegalArgumentException("Уроверь спортивной школы может быть от 0 до 10");
        }
        int age = RANDOM.nextInt(BETWEEN_YOUNG_AGE) + MIN_AGE;
        int averageByMinAge = RANDOM.nextInt(BETWEEN_YOUNG_COMMON) + MIN_COMMON + sportschoolLevel;
        Player player = new Player(LocalPosition.generatePosition(), averageByMinAge, age,
                NamesStore.getInstance().getRandomFirstName(),
                NamesStore.getInstance().getRandomLastName());
        if (age > MIN_AGE) {
            for (int i = 0; i < age - MIN_AGE; i++) {
                player.addAge();
            }
        }
        return player;
    }

    /**
     * Получает случайное целое число от left до right с нормальный
     * распределением.
     *
     * @param left
     * @param right
     * @return
     */
    public static int generateGaussian(int left, int right) {
        double g = RANDOM.nextGaussian();
        double gamma = 3;//параметр, который определяет "разброс" значений: 
        //чем больше гамма, тем ближе к центру
        if (g > gamma) {
            g = gamma;
        }
        if (g < -gamma) {
            g = -gamma;
        }
        double center = ((double) (right - left)) / 2;
        double gdisperse = center / gamma;
        g *= gdisperse;
        g = g + left + center;
        return (int) Math.round(g);
    }

    /**
     * Получает случайное среднее игрока от MIN до MAX, центр распределения
     * смещен влево. Зависит от уровня спортивной школы.
     *
     * @param sportschoolLevel
     * @return
     */
    public static int randomNormalLeftAverage(int sportschoolLevel) {
        int min = MIN_COMMON + sportschoolLevel;
        int midRight = (MAX_YOUNG_COMMON + MID_YOUNG_COMMON) / 2 + sportschoolLevel;
        int max = MAX_YOUNG_COMMON + sportschoolLevel;
        int avgMinMid = generateGaussian(min, midRight);
        int avgMaxMid = generateGaussian(min, max);
        int midLeft;
        if (avgMaxMid <= midRight) {
            midLeft = (avgMinMid + avgMaxMid) / 2;
        } else {
            midLeft = avgMaxMid;
        }
        return midLeft;
    }

    public static Player createPlayer(LocalPosition localPosition, MasteryLevel level) {
        if (level == null) {
            level = MasteryLevel.MID;
        }
        int age = RANDOM.nextInt(BETWEEN_AGE) + MAX_YOUNG_AGE;
        int left = level.getMin();
        int right = level.getMax();
        int average = generateGaussian(left, right);
        Player player = new Player(localPosition, average, age,
                NamesStore.getInstance().getRandomFirstName(),
                NamesStore.getInstance().getRandomLastName());
        return player;
    }

    public static Player createPlayer(GlobalPosition position, MasteryLevel level) {
        EnumSet<LocalPosition> localPositions = position.getLocalPositions();
        List<LocalPosition> positions = new ArrayList<>(localPositions);
        LocalPosition localPosition;
        if (localPositions.size() == 1) {
            localPosition = positions.get(0);
        } else {
            localPosition = positions.get(RANDOM.nextInt(positions.size()));
        }
        return createPlayer(localPosition, level);
    }

    /**
     * Создает молодого игрока в возрасте от 16 до 21 года с выбранным амплуа.
     *
     * @param sportschoolLevel from 0 to 10
     * @param position
     * @return
     */
    public static Player createYoungPlayer(int sportschoolLevel, GlobalPosition position) {
        if (sportschoolLevel < 0 || sportschoolLevel > 10) {
            throw new IllegalArgumentException("Уроверь спортивной школы может быть от 0 до 10");
        }
        EnumSet<LocalPosition> localPositions = position.getLocalPositions();
        List<LocalPosition> positions = new ArrayList<>(localPositions);
        LocalPosition localPosition;
        if (localPositions.size() == 1) {
            localPosition = positions.get(0);
        } else {
            localPosition = positions.get(RANDOM.nextInt(positions.size()));
        }
        int age = RANDOM.nextInt(BETWEEN_YOUNG_AGE) + MIN_AGE;
        TalentType talentType = TalentType.getByProbability();
        int averageByMinAge = RANDOM.nextInt(BETWEEN_YOUNG_COMMON) + MIN_COMMON + sportschoolLevel;
        int average = averageByMinAge;
        if (age > MIN_AGE) {
            for (int i = 0; i < age - MIN_AGE; i++) {
                average += PlayerProgressParams.CONSTANTS.get(talentType).get(i);
            }
        }
        Player player = new Player(localPosition, average, age,
                NamesStore.getInstance().getRandomFirstName(),
                NamesStore.getInstance().getRandomLastName());
        return player;
    }

}
