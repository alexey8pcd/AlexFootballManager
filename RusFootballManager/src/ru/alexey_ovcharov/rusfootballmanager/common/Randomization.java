package ru.alexey_ovcharov.rusfootballmanager.common;

import java.util.Random;

/**
 * @author Alexey
 */
public class Randomization {

    public static final Random RANDOM = new Random();

    /**
     * Получает случайное число от base до base + disperse
     *
     * @param base
     * @param disperse
     * @return
     */
    public static int getValueByBase(int base, int disperse) {
        return RANDOM.nextInt(disperse) + base;
    }

    public static int getValueInBounds(int from, int to) {
        return RANDOM.nextInt(to - from) + from;
    }
}
