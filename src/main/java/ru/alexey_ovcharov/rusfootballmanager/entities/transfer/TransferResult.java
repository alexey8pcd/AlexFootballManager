package ru.alexey_ovcharov.rusfootballmanager.entities.transfer;

public enum TransferResult {
    /**
     * На рассмотрении
     */
    UNDER_CONSIDERATION("На рассмотрении"),
    ACCEPT("Согласовано"),
    DECLINE("Отклонено"),
    /**
     * Игрок под под сомнением, не устраивает зарплата
     */
    AMBIGUOUS("Игрок не принял решение о переходе"),
    /**
     * Команду-продавец не устраивает цена
     */
    MORE_SUM("Не устраивает цена за игрока");
    private final String description;

    TransferResult(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
