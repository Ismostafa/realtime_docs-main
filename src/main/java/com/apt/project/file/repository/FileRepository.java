package com.apt.project.file.repository;

import com.apt.project.file.model.File;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends MongoRepository<File, String> {
    Optional<File> findByName(String name);
    List<File> findAllByOwnerId(String ownerId);
}
