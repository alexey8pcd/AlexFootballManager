package rusfootballmanager.entities.player;

public enum Characteristic {
    POSITION_CHOISE("Выбор позиции"),
    REACTION("Реакция"),
    REFLECTION("Рефлексы"),
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

    private final String name;

    private Characteristic(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
