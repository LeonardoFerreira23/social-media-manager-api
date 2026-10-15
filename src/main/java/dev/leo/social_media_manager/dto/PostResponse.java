package dev.leo.social_media_manager.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record PostResponse(
        Long id,
        String title,
        String caption,
        @JsonFormat(pattern = "dd/MM/yyyy") LocalDate date,
        @JsonProperty("isPublished") boolean isPublished
) {}