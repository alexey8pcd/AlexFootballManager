package rusfootballmanager;

import java.util.Random;

/**
 * @author Alexey
 */
public class Common {

    public static final Random RANDOM = new Random();

    public static int getRandomValue(int base, int disperse) {
        return RANDOM.nextInt(disperse) + base;
    }
}
