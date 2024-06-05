package com.apt.project.permission.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "permissions")
public class Permission {
    @Id
    private String id;

    @NotBlank
    private String userId;

    @NotBlank
    private String fileId;

    @NotBlank
    private Role role;

    public Permission(String userId, String fileId, Role role) {
        this.userId = userId;
        this.fileId = fileId;
        this.role = role;
    }
}
