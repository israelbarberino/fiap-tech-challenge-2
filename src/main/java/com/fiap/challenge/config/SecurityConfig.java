package com.fiap.challenge.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${management.security.username:admin}")
    private String actuatorUsername;

    @Value("${management.security.password:}")
    private String actuatorPassword;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public UserDetailsService actuatorUserDetailsService() {
        
        String password = actuatorPassword.isEmpty()  ? "CHANGE_ME_IN_PRODUCTION" : actuatorPassword;
            
        UserDetails actuatorAdmin = User.builder()
                .username(actuatorUsername)
                .password(passwordEncoder().encode(password))
                .roles("ACTUATOR_ADMIN")
                .build();
        return new InMemoryUserDetailsManager(actuatorAdmin);
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/**").hasRole("ACTUATOR_ADMIN")
                        .requestMatchers("/api/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .anyRequest().permitAll()
                )
                .httpBasic(basic -> {});
        return http.build();
    }
}
