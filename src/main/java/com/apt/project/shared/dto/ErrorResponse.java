package com.apt.project.shared.dto;

public record ErrorResponse
        (int statusCode,
         String message){}
