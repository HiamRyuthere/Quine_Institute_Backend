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
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "*")
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                //**TESTING**
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
                    corsConfiguration.setAllowedOrigins(java.util.List.of("*")); // Sab origins allowed (Dev ke liye okay hai)
                    corsConfiguration.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfiguration.setAllowedHeaders(java.util.List.of("*"));
                    return corsConfiguration;
                }))
                // **TESTING**
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Public Endpoints
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v1/notices/**").permitAll()
                        .requestMatchers("/api/v1/admissions/apply").permitAll()

                        // Protected Employee Management (Admin only)
                        .requestMatchers("/api/v1/employees/").hasRole("ADMIN")// Currently permitAll for initial setup
                        .requestMatchers("/api/v1/employees/**").authenticated()
                        //Section Management
                        // Section Management
                        .requestMatchers(HttpMethod.POST, "/api/v1/sections").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/sections").hasAnyRole("ADMIN", "FACULTY")
                        // Admission Management
                                .requestMatchers("/api/v1/admissions/apply").permitAll()
                                .requestMatchers("/api/v1/admissions/approve/**", "/api/v1/admissions/pending").hasRole("ADMIN")
                                .requestMatchers("/api/v1/admissions/**").hasRole("ADMIN")

                        // Student Management
                        .requestMatchers(HttpMethod.POST, "/api/v1/students").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/students").hasAnyRole("ADMIN", "FACULTY")
                        .requestMatchers("/api/v1/students/**").authenticated()

                        //Program Endpoints
                        .requestMatchers(HttpMethod.POST, "/api/v1/programs").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/v1/programs").permitAll()

                        //**TESTING PAYMENT TESTING PAYMENT//
                        .requestMatchers("/api/v1/payments/**").authenticated()

                        //**TESTING PAYMENT TESTING PAYMENT
                        // Default rule
                        .anyRequest().authenticated()


                )
                // Establish Stateless session management
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // Register the JWT filter before the standard UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}