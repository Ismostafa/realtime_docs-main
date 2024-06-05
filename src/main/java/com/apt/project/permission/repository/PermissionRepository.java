package com.apt.project.permission.repository;

import com.apt.project.permission.model.Permission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends MongoRepository<Permission, String> {
    Optional<Permission> findByFileIdAndUserId(String fileId, String userId);

    List<Permission> findAllByUserId(String userId);

    Permission deleteByFileId(String fileId);
}
