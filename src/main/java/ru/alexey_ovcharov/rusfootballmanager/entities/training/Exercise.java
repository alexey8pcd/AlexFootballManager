package ru.alexey_ovcharov.rusfootballmanager.entities.training;

import ru.alexey_ovcharov.rusfootballmanager.entities.player.Characteristic;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.CharacteristicsBuilder;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.LocalPosition;

public enum Exercise {

    PASS_ACCURACY_TRAINING(1, "Точность передач", Characteristic.PASS_ACCURACY),
    SET_PIECE(3, "Стандартные положения", Characteristic.INTELLIGENCE,
            Characteristic.PASS_ACCURACY, Characteristic.SHOT_POWER),
    SHOT_TRAINING(2, "Отработка ударов", Characteristic.SHOT_ACCURACY, Characteristic.SHOT_POWER),
    GOALKEEPER_TRAINING(3, "Тренировка вратарей",
            CharacteristicsBuilder.getPrimaryChars(LocalPosition.GOALKEEPER).toArray(new Characteristic[0])),
    PRESSURE_TRAINING(4, "Прессинг", Characteristic.BALANCE, Characteristic.POSITION_CHOISE, Characteristic.INTELLIGENCE),
    TACKLE_TRAINING(2, "Отбор мяча", Characteristic.BALANCE, Characteristic.TACKLE),
    DRIBBLING_TRAINING(2, "Дрибблинг", Characteristic.BALL_CONTROL, Characteristic.DRIBBLING),
    SPEED_TRAINING(2, "Скоростная тренировка", Characteristic.SPEED, Characteristic.ACCELERATION),
    STAMINA_TRAINING(1, "Тренировка выносливости", Characteristic.STAMINA);

    private final int hours;
    private final String description;
    private final Characteristic[] characteristics;

    Exercise(int hours, String description, Characteristic... characteristics) {
        this.hours = hours;
        this.description = description;
        this.characteristics = characteristics;
    }

    public int getHours() {
        return hours;
    }

    public String getDescription() {
        return description;
    }

    public String view() {
        return description + " (" + hours + " ч)";
    }

    public Characteristic[] getCharacteristics() {
        return characteristics;
    }
}
