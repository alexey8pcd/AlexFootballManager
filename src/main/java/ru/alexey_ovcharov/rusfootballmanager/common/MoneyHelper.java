package ru.alexey_ovcharov.rusfootballmanager.common;

import ru.alexey_ovcharov.rusfootballmanager.entities.player.Player;

import javax.annotation.Nonnull;
import java.text.NumberFormat;

/**
 * @author Alexey
 */
public class MoneyHelper {

    private static final int PAY_AVG_FACTOR = 20;
    private static final int PAY_AGE_FACTOR = 3;
    private static final double B = 0.2;
    private static final int A = 20;
    private static final int COST_AVG_BASE_VALUE = 7;
    private static final int COST_AVG_FACTOR = 120;
    private static final double COST_AVG_POWER_COEFF = 2.15;
    private static final long TICKETS_COEFF = 400;
    private static final int BASE_TICKETS = 10;

    public static int calculatePayForMatch(Player player) {
        return calculatePayForMatch(player.getAge(), player.getAverage());
    }

    private static int calculatePayForMatch(int age, int avg) {
        int top = avg * avg * avg * PAY_AVG_FACTOR;
        int down = age * age / PAY_AGE_FACTOR;
        return top / down;
    }

    public static int calculateTransferCost(int age, int avg) {
        double sqrt = Math.sqrt(avg - COST_AVG_BASE_VALUE);
        double top = Math.pow(2, sqrt * COST_AVG_POWER_COEFF) * COST_AVG_FACTOR;
        double down = (double) age / A + B;
        return (int) Math.round(top / down);
    }

    public static long calculateMoneyFromTickets(int stadiumManager, int support) {
        return TICKETS_COEFF * (BASE_TICKETS + stadiumManager) * support;
    }

    @Nonnull
    public static String formatSum(long sum) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setGroupingUsed(true);
        return numberFormat.format(sum);
    }

    private MoneyHelper() {

    }

    public static int calculateTransferCost(Player player) {
        return calculateTransferCost(player.getAge(), player.getAverage());
    }
}
