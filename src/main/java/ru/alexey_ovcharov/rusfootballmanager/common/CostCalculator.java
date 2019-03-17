package ru.alexey_ovcharov.rusfootballmanager.common;

import ru.alexey_ovcharov.rusfootballmanager.entities.player.Player;

/**
 * @author Alexey
 */
public class CostCalculator {

    private static final int PAY_AVG_FACTOR = 50;
    private static final int PAY_AGE_FACTOR = 3;
    private static final double B = 0.2;
    private static final int A = 20;
    private static final int COST_AVG_BASE_VALUE = 7;
    private static final int COST_AVG_FACTOR = 120;
    private static final double COST_AVG_POWER_COEFF = 2.15;

    public static int calculatePayForMatch(Player player) {
        return calculatePayForMatch(player.getAge(), player.getAverage(player.getPreferredPosition()));
    }

    private static int calculatePayForMatch(int age, int avg) {
        int top = avg * avg * avg * PAY_AVG_FACTOR;
        int down = age * age / PAY_AGE_FACTOR;
        return top / down;
    }

    public static int calculateTransferCost(int age, int avg) {
        double top = Math.pow(2, Math.sqrt(avg - COST_AVG_BASE_VALUE)
                * COST_AVG_POWER_COEFF) * COST_AVG_FACTOR;
        double down = (double) age / A + B;
        return (int) Math.round(top / down);
    }

    private CostCalculator() {

    }

}
