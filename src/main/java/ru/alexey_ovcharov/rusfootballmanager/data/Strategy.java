package ru.alexey_ovcharov.rusfootballmanager.data;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

public enum Strategy {

    SUPER_ATTACK(2, "Рискованная атака"),
    ATTACK(1, "Атака"),
    BALANCE(0, "Баланс"),
    DEFENCE(-1, "Оборона"),
    SUPER_DEFENCE(-2, "Плотная оборона");

    private final int value;
    private final String description;

    Strategy(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    @Nonnull
    public static Strategy getByDescription(@Nullable String description) throws IllegalArgumentException {
        return Arrays.stream(values())
                     .filter(strategy -> strategy.description.equals(description))
                     .findFirst()
                     .orElseThrow(IllegalArgumentException::new);
    }
}
