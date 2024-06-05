package com.apt.project.file.dto;

import jakarta.validation.constraints.NotBlank;

public record NewFileDto(
        @NotBlank
        String fileName) {
}
