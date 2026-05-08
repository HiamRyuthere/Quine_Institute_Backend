package com.quine.backend.config;

import com.quine.backend.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@CrossOrigin(origins = "*")
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // ── CORS ──────────────────────────────────────────────────────────
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("*"));
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(List.of("*"));
                    return config;
                }))

                // ── CSRF (disabled — JWT use ho raha hai) ─────────────────────────
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        // ══════════════════════════════════════════════════════════════
                        // PUBLIC — No token needed
                        // ══════════════════════════════════════════════════════════════
                        .requestMatchers("/api/v1/auth/**").permitAll()

                        // Notices — GET public, POST sirf ADMIN
                        .requestMatchers(HttpMethod.GET,  "/api/v1/notices").permitAll()
                        .requestMatchers(HttpMethod.GET,  "/api/v1/notices/archive").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/notices").hasRole("ADMIN")

                        // Programs — GET public, POST sirf ADMIN
                        .requestMatchers(HttpMethod.GET,  "/api/v1/programs").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/programs").hasRole("ADMIN")

                        // Applications — apply public, baaki ADMIN only
                        // (Controller path: /api/v1/applications — NOT /admissions)
                        .requestMatchers(HttpMethod.POST, "/api/v1/applications/apply").permitAll()
                        .requestMatchers(HttpMethod.GET,  "/api/v1/applications/pending").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,  "/api/v1/applications/approve/**").hasRole("ADMIN")

                        // ══════════════════════════════════════════════════════════════
                        // EMPLOYEES — Role-based
                        // ══════════════════════════════════════════════════════════════
                        .requestMatchers(HttpMethod.POST, "/api/v1/employees").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/api/v1/employees").hasAnyRole("ADMIN", "FACULTY")
                        .requestMatchers(HttpMethod.GET,  "/api/v1/employees/**").authenticated()

                        // ══════════════════════════════════════════════════════════════
                        // STUDENTS — Role-based
                        // ══════════════════════════════════════════════════════════════
                        .requestMatchers(HttpMethod.POST, "/api/v1/students").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/api/v1/students").hasAnyRole("ADMIN", "FACULTY")
                        .requestMatchers(HttpMethod.GET,  "/api/v1/students/**").authenticated() // Security check service layer mein hai

                        // ══════════════════════════════════════════════════════════════
                        // SEMESTERS
                        // ══════════════════════════════════════════════════════════════
                        .requestMatchers(HttpMethod.POST, "/api/v1/semesters").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/api/v1/semesters").authenticated()
                        .requestMatchers(HttpMethod.GET,  "/api/v1/semesters/**").authenticated()

                        // ══════════════════════════════════════════════════════════════
                        // SUBJECTS
                        // ══════════════════════════════════════════════════════════════
                        .requestMatchers(HttpMethod.POST, "/api/v1/subjects").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/api/v1/subjects").authenticated()
                        .requestMatchers(HttpMethod.GET,  "/api/v1/subjects/**").authenticated()

                        // ══════════════════════════════════════════════════════════════
                        // EXAM RESULTS
                        // ══════════════════════════════════════════════════════════════
                        // Internal marks → FACULTY ; External marks → EXAMINER ; Admin can do both
                        .requestMatchers(HttpMethod.POST, "/api/v1/results/add").hasAnyRole("ADMIN", "FACULTY", "EXAMINER")
                        .requestMatchers(HttpMethod.GET,  "/api/v1/results/**").authenticated()

                        // ══════════════════════════════════════════════════════════════
                        // MENTORS
                        // ══════════════════════════════════════════════════════════════
                        .requestMatchers(HttpMethod.POST, "/api/v1/mentors").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/api/v1/mentors/**").authenticated()

                        // ══════════════════════════════════════════════════════════════
                        // PAYMENTS
                        // ══════════════════════════════════════════════════════════════
                        .requestMatchers("/api/v1/payments/**").authenticated()

                        // ══════════════════════════════════════════════════════════════
                        // DEFAULT — Sab kuch authenticated
                        // ══════════════════════════════════════════════════════════════
                        .anyRequest().authenticated()
                )

                // Stateless sessions (JWT hai, server session ki zarurat nahi)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // JWT filter pehle chalega
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}