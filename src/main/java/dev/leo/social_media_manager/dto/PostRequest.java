package dev.leo.social_media_manager.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PostRequest(
        @NotBlank String title,
        @NotBlank String caption,
        @NotNull @JsonFormat(pattern = "dd/MM/yyyy") LocalDate date,
        @NotNull @JsonProperty("isPublished") Boolean isPublished
) {}