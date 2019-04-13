package ru.alexey_ovcharov.rusfootballmanager.career;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author Alexey
 */
public class Message {

    private final String from;
    private final LocalDate date;
    private final String theme;
    private final String body;

    public Message(String from, LocalDate date, String theme, String body) {
        this.from = from;
        this.date = date;
        this.theme = theme;
        this.body = body;
    }

    public String getFrom() {
        return from;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getTheme() {
        return theme;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Date: ")
               .append(date.format(DateTimeFormatter.BASIC_ISO_DATE));
        builder.append(", ")
               .append(" From: ")
               .append(from)
               .append(", ");
        builder.append("Theme: ").append(theme);
        return builder.toString();
    }

}
