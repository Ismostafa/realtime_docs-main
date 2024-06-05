package com.apt.project.file.dto;

import com.apt.project.file.model.File;
import com.apt.project.permission.model.Role;

import java.time.LocalDateTime;

public record FileDto(
         String id,
         LocalDateTime createdAt,
         String name,
         String ownerId,
         Role role
) {
    public static FileDto from(File file,Role role) {
        return new FileDto(file.getId(), file.getCreatedAt(), file.getName(), file.getOwnerId(),role);
    }
}
