package ru.alexey_ovcharov.rusfootballmanager.common;

public class FatalError extends RuntimeException {
    public FatalError(Throwable cause) {
        super(cause);
    }
}
