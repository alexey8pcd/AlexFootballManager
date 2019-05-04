package ru.alexey_ovcharov.rusfootballmanager.entities.player;

import ru.alexey_ovcharov.rusfootballmanager.common.Randomization;

import java.time.LocalDate;

public class Injure {

    private static final int HUNDRED = 100;
    private final InjureType injureType;
    private final LocalDate injureDate;
    private float daysRestoration;
    private final LocalDate endDate;

    private Injure(InjureType injureType, LocalDate injureDate) {
        this.injureType = injureType;
        this.injureDate = injureDate;
        this.daysRestoration = injureType.getDaysRestoration();
        this.endDate = injureDate.plusDays(injureType.getDaysRestoration());
    }

    public static Injure random(LocalDate matchDate) {
        return new Injure(InjureType.getInjure(Randomization.nextInt(HUNDRED)), matchDate);
    }

    public void restore(int doctor) {
        daysRestoration -= (1 + (doctor - 1) * 0.05);
    }

    public boolean isEnd() {
        return daysRestoration <= 0;
    }

    public InjureType getInjureType() {
        return injureType;
    }

    public LocalDate getInjureDate() {
        return injureDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getDescription() {
        return injureType.getDescription();
    }
}
