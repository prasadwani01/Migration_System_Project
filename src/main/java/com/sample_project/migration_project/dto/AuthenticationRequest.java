package com.sample_project.migration_project.dto;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String username;
    private String password;
}