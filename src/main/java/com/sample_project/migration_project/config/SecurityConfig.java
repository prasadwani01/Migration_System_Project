package com.sample_project.migration_project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Usually disabled for stateless REST APIs
                .authorizeHttpRequests(auth -> auth
                        // Allow public access to Swagger UI and API Docs
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/api/v1/auth/**"
                        ).permitAll()
                        // 2. Institution Only
                        .requestMatchers("/api/v1/institution/**").hasRole("INSTITUTION")

                        // 3. Admin & Institution Bulk Data Access
                        .requestMatchers(
                                org.springframework.http.HttpMethod.GET,
                                "/api/v1/migrants",
                                "/api/v1/migrants/paginated",
                                "/api/v1/migrants/export/report/pdf",
                                "/api/v1/migrants/export/pdf"
                                        ).hasAnyRole("ADMIN", "INSTITUTION")

                        // 4. Admin & Institution Creation/Upload Access
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/v1/migrants/**").hasAnyRole("ADMIN", "INSTITUTION")

                        // 5. Everything else requires login
                        .anyRequest().authenticated()
                )
                // Tell Spring not to save session state (since we use JWTs)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Add the JWT filter to the chain before the standard authentication filter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        // (You likely have your JWT filter setup here as well)

        return http.build();
    }
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable()) // Usually disabled for stateless REST APIs
//                .authorizeHttpRequests(auth -> auth
//                        // Allow public access to Swagger UI and API Docs
//                        .requestMatchers(
//                                "/v3/api-docs/**",
//                                "/swagger-ui/**",
//                                "/swagger-ui.html",
//                                "/api/v1/migrants/export/pdf",// <-- Add this line to allow public downloads
//                                "/api/v1/auth/**"
//                        ).permitAll()
//                        // Allow public access to Auth endpoints (if you have them)
//                        .requestMatchers("/api/v1/auth/**").permitAll()
//                        // Require authentication for everything else
//                        .anyRequest().authenticated()
//                )
//                // Tell Spring not to save session state (since we use JWTs)
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                // Add the JWT filter to the chain before the standard authentication filter
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//        // (You likely have your JWT filter setup here as well)
//
//        return http.build();
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}