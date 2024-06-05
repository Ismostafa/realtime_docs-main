package com.apt.project.permission.service;

import com.apt.project.permission.dto.NewPermission;
import com.apt.project.permission.model.Permission;
import com.apt.project.permission.repository.PermissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {
    final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public void newPermission(NewPermission newPermission) {
        Permission permission = new Permission(newPermission.userId(), newPermission.fileId(), newPermission.role());
        permissionRepository.save(permission);
    }

    public Optional<Permission> findByFileIdAndUserId(String fileId, String userId){
        return permissionRepository.findByFileIdAndUserId(fileId, userId);
    }


    public List<Permission> findAllByUserId(String userId) {
        return permissionRepository.findAllByUserId(userId);
    }

    public void deleteByFileId(String fileId) {
        permissionRepository.deleteByFileId(fileId);
    }

}
