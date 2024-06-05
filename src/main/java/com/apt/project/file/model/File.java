package com.apt.project.file.model;

import lombok.Data;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(value = "files")
@Data
public class File {
    @Id
    private String id;
    private String ownerId;
    private LocalDateTime createdAt;
    @Setter
    private String name;
    @Setter
    private String content;

    public File(String ownerId, String name) {
        this.ownerId = ownerId;
        this.name = name;
        this.createdAt = LocalDateTime.now();
    }

}
