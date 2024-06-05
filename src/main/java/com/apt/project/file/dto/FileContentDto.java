package com.apt.project.file.dto;

import com.apt.project.file.model.File;

public record FileContentDto(
        String id,
        String content
) {
    public static FileContentDto from(File file) {
        return new FileContentDto(file.getId(),file.getContent());
    }
}
