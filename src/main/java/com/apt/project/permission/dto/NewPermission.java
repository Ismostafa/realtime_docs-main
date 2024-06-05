package com.apt.project.permission.dto;

import com.apt.project.permission.model.Role;

public record NewPermission(String userId, String fileId, Role role) {
}

