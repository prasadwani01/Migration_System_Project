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

// --- NEW IMPORTS REQUIRED FOR CORS ---
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import java.util.List;

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
                // 1. ADDED THIS LINE FIRST: Enable the CORS rules defined in the bean below
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .csrf(csrf -> csrf.disable()) // Usually disabled for stateless REST APIs
                .authorizeHttpRequests(auth -> auth
                        // Allow public access to Swagger UI, API Docs, Auth, and WebSockets
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/api/v1/auth/**",
                                "/ws-location/**"  // <-- ADDED THIS LINE FOR REAL-TIME MAPS
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

        return http.build();
    }

    // 2. ADDED THIS ENTIRE NEW BEAN: The Global CORS Configuration
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // The exact URLs where the frontend team is running their local servers
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:5173","https://migration-frontend-7erdi2mo0-prasadwani01-8281s-projects.vercel.app/"));

        // Allow standard HTTP methods
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Allow all headers (Crucial for the frontend to send the Authorization: Bearer token)
        configuration.setAllowedHeaders(List.of("*"));

        // Allow credentials (necessary for tokens/auth headers)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // Apply these rules to every API endpoint
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}