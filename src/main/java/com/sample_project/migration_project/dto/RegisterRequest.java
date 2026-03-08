package com.sample_project.migration_project.dto;

import com.sample_project.migration_project.model.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private Role role;
}