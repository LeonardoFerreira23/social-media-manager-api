package dev.leo.social_media_manager.exception;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FutureDateException extends RuntimeException {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public FutureDateException(LocalDate date) {
        super("Post date cannot be in the future: " + date.format(FORMATTER));
    }
}