package ru.alexey_ovcharov.rusfootballmanager.entities;

import ru.alexey_ovcharov.rusfootballmanager.career.Message;

import java.time.LocalDate;

public interface MessageConsumer {
    LocalDate getCurrentDate();

    void addMessage(Message message);
}
