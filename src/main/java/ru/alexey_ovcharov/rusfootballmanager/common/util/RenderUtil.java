package ru.alexey_ovcharov.rusfootballmanager.common.util;

import java.awt.Color;
import java.awt.Graphics;

/**
 * @author Alexey
 */
public class RenderUtil {

    public static final Color LIGHT_LIGHT_GRAY = new Color(216, 216, 216);

    public static Color getPlayerAverageColor(int val) {
        if (val >= 90) {
            return Color.CYAN;
        } else if (val >= 80) {
            return Color.GREEN;
        } else if (val >= 70) {
            return Color.YELLOW;
        } else if (val >= 60) {
            return Color.ORANGE;
        } else if (val >= 50) {
            return Color.PINK;
        } else if (val >= 40) {
            return Color.LIGHT_GRAY;
        } else if (val >= 30) {
            return LIGHT_LIGHT_GRAY;
        }
        return Color.WHITE;
    }
}
