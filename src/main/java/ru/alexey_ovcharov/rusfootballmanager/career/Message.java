package ru.alexey_ovcharov.rusfootballmanager.career;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Alexey
 */
public class Message {

    private String from;
    private Date date;
    private String theme;
    private String body;
    private static final String datePattern = "dd.MM.yyyy";

    public Message(String from, Date date, String theme, String body) {
        this.from = from;
        this.date = date;
        this.theme = theme;
        this.body = body;
    }

    public String getFrom() {
        return from;
    }

    public Date getDate() {
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
        builder.append("Date: ").append(new SimpleDateFormat(datePattern).format(date));
        builder.append(", ").append(" From: ").append(from).append(", ");
        builder.append("Theme: ").append(theme);
        return builder.toString();
    }

}
