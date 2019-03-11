package ru.alexey_ovcharov.rusfootballmanager.data;

public enum Trick {
    MINOR_FAULT("Мелкий фол"),
    SAFETY("Подстраховка"),
    POWER_SAVING("Экономия сил"),
    EARLY_SUBSTITUTIONS("Ранние замены"),
    BREAKTHROUGH_AND_FEED("Прорыв и подача");
    private final String description;

    Trick(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
