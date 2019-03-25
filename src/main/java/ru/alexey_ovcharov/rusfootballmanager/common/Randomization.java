package ru.alexey_ovcharov.rusfootballmanager.common;

import java.util.Random;

/**
 * @author Alexey
 */
public class Randomization {

    private static final Random RANDOM = new Random();

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

    private Randomization() {

    }

    public static double nextGaussian() {
        return RANDOM.nextGaussian();
    }

    public static int nextInt(int bound) {
        return RANDOM.nextInt(bound);
    }

    /**
     * @return from 0.0 to 1.0
     */
    public static double nextDouble() {
        return RANDOM.nextDouble();
    }

    /**
     * @return from 0.0 to scale
     */
    public static double nextDoubleScaled(double scale) {
        return RANDOM.nextDouble() * scale;
    }
}
