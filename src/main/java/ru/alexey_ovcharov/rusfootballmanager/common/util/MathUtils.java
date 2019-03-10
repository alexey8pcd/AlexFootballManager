package ru.alexey_ovcharov.rusfootballmanager.common.util;

import ru.alexey_ovcharov.rusfootballmanager.common.Randomization;

/**
 * @author Alexey
 */
public class MathUtils {

    /**
     * Вычисляет двоичный логарифм числа, затем округляет
     *
     * @param value
     * @return
     */
    public static int log2(long value) {
        return (int) (Math.log(value) / Math.log(2));
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
        double g = Randomization.nextGaussian();
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

    private MathUtils() {

    }
}
