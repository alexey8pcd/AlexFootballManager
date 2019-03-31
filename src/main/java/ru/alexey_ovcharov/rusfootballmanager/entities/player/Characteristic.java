package ru.alexey_ovcharov.rusfootballmanager.entities.player;

import ru.alexey_ovcharov.rusfootballmanager.common.Randomization;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

public enum Characteristic {
    POSITION_CHOISE("Выбор позиции"),
    REACTION("Реакция"),
    REFLECTION("Рефлексы"),
    /**
     * Самообладание
     */
    COMPOSURE("Самообладание"),
    TACKLE("Отбор мяча"),
    BALANCE("Баланс при отборе"),
    SPEED("Скорость"),
    ACCELERATION("Ускорение"),
    STAMINA("Запас сил"),
    PASS_ACCURACY("Точность передач"),
    SHOT_ACCURACY("Точность удара"),
    SHOT_POWER("Сила удара"),
    INTELLIGENCE("Интеллектуальность"),
    BALL_CONTROL("Контроль мяча"),
    DRIBBLING("Ведение мяча"),
    MENTAL_STABILITY("Устойчивость");

    private static final int MIN_CHAR_VALUE = 20;
    private static final int SECONDARY_DEFAULT_DISPERSE_VALUE = 10;
    private static final int PRIMARY_DEFAULT_DISPERSE_VALUE = 15;
    static final int MAX_CHAR_VALUE = 99;
    private final String name;

    Characteristic(String name) {
        this.name = name;
    }

    @Nonnull
    static Map<Characteristic, Integer> buildPlayerChars(@Nonnull LocalPosition preferredPosition, int average) {
        Map<Characteristic, Integer> chars = new EnumMap<>(Characteristic.class);
        for (Characteristic ch : CharacteristicsBuilder.getSecondaryChars(preferredPosition)) {
            chars.put(ch, Randomization.getValueByBase(
                    MIN_CHAR_VALUE, SECONDARY_DEFAULT_DISPERSE_VALUE));
        }
        Set<Characteristic> primaryChars = CharacteristicsBuilder.getPrimaryChars(preferredPosition);
        int[] values = new int[primaryChars.size()];
        Arrays.fill(values, average);
        int length = values.length;
        for (int i = 0; i < length; i++) {
            int randValue = Randomization.getValueByBase(0, PRIMARY_DEFAULT_DISPERSE_VALUE);
            values[i] += randValue;
            values[length - 1 - i] -= randValue;
            if (values[i] > MAX_CHAR_VALUE) {
                int diff = values[i] - MAX_CHAR_VALUE;
                values[i] -= diff;
                values[length - 1 - i] += diff;
            }

        }
        int index = 0;
        for (Characteristic ch : primaryChars) {
            chars.put(ch, values[index++]);
        }
        return chars;
    }

    public String getName() {
        return name;
    }

}
