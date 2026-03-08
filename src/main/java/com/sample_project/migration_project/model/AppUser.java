package com.sample_project.migration_project.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
//@SuppressWarnings("JpaDataSourceORMInspection")
@Table(name = "users")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username; // You can store the Mobile Number here for OTP login

    @Column(nullable = false)
    private String password; // Spring Security will store the encrypted hash here

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
}