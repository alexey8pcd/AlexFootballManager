package ru.alexey_ovcharov.rusfootballmanager.entities.school;

import ru.alexey_ovcharov.rusfootballmanager.common.NamesStore;
import ru.alexey_ovcharov.rusfootballmanager.common.Randomization;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.*;
import ru.alexey_ovcharov.rusfootballmanager.entities.team.Personal;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static ru.alexey_ovcharov.rusfootballmanager.common.util.MathUtils.generateGaussian;
import static ru.alexey_ovcharov.rusfootballmanager.entities.player.Player.*;

/**
 * @author Alexey
 */
public class PlayerCreator {

    private static final int MIN_COMMON = 20;
    private static final int MAX_YOUNG_COMMON = 40;

    private static final int MID_YOUNG_COMMON = (MAX_YOUNG_COMMON + MIN_COMMON) / 2;
    private static final int BETWEEN_YOUNG_COMMON = MAX_YOUNG_COMMON - MIN_COMMON;

    private static final int BETWEEN_YOUNG_AGE = MAX_YOUNG_AGE - MIN_AGE;
    private static final int BETWEEN_AGE = MAX_AGE - MAX_YOUNG_AGE;

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
        if (sportschoolLevel < 0 || sportschoolLevel > Personal.MAX_LEVEL) {
            throw new IllegalArgumentException("Уроверь спортивной школы может быть от 0 до 9");
        }
        int age = Randomization.nextInt(BETWEEN_YOUNG_AGE) + MIN_AGE;
        int averageByMinAge = Randomization.nextInt(BETWEEN_YOUNG_COMMON) + MIN_COMMON
                + sportschoolLevel * 2;
        Player player = new Player(LocalPosition.generatePosition(), averageByMinAge, MIN_AGE,
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

    @Nonnull
    public static Player createPlayer(LocalPosition localPosition, MasteryLevel level) {
        if (level == null) {
            level = MasteryLevel.MID;
        }
        int age = Randomization.nextInt(BETWEEN_AGE) + MAX_YOUNG_AGE;
        int left = level.getMin();
        int right = level.getMax();
        int average = generateGaussian(left, right);
        return new Player(localPosition, average, age,
                NamesStore.getInstance().getRandomFirstName(),
                NamesStore.getInstance().getRandomLastName());
    }

    @Nonnull
    public static Player createPlayer(GlobalPosition position, MasteryLevel level) {
        Set<LocalPosition> localPositions = position.getLocalPositions();
        List<LocalPosition> positions = new ArrayList<>(localPositions);
        LocalPosition localPosition;
        if (localPositions.size() == 1) {
            localPosition = positions.get(0);
        } else {
            localPosition = positions.get(Randomization.nextInt(positions.size()));
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
        Set<LocalPosition> localPositions = position.getLocalPositions();
        List<LocalPosition> positions = new ArrayList<>(localPositions);
        LocalPosition localPosition;
        if (localPositions.size() == 1) {
            localPosition = positions.get(0);
        } else {
            localPosition = positions.get(Randomization.nextInt(positions.size()));
        }
        int age = Randomization.nextInt(BETWEEN_YOUNG_AGE) + MIN_AGE;
        TalentType talentType = TalentType.getByProbability();
        int average = Randomization.nextInt(BETWEEN_YOUNG_COMMON) + MIN_COMMON + sportschoolLevel;
        if (age > MIN_AGE) {
            for (int i = 0; i < age - MIN_AGE; i++) {
                average += ProgressParameters.CONSTANTS.get(talentType).get(i);
            }
        }
        return new Player(localPosition, average, age,
                NamesStore.getInstance().getRandomFirstName(),
                NamesStore.getInstance().getRandomLastName());
    }

    private PlayerCreator() {

    }

}
