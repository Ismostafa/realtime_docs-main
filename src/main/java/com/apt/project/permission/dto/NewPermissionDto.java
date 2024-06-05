package com.apt.project.permission.dto;

import com.apt.project.permission.model.Role;

public record NewPermissionDto(String username, String fileId, Role role) {
}