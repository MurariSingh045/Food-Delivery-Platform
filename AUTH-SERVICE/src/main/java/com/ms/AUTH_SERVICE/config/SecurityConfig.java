package com.ms.AUTH_SERVICE.config;

import com.ms.AUTH_SERVICE.filter.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {

         http.csrf(csrf -> csrf.disable())
                 .authorizeHttpRequests(auth -> auth
                         .requestMatchers("/admin/**").hasRole("ADMIN")
                         .requestMatchers("/restaurant-role/**").permitAll()
                         .requestMatchers("/auth/login" , "/auth/register" , "/auth/validate").permitAll()
                         .anyRequest().authenticated()
                 )
                  // make token stateless
                 .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

                // ensure jwtAuthFilter runs right before login filter(UsernamePasswordAuthenticationFilter)
                 http.addFilterBefore(jwtAuthFilter , UsernamePasswordAuthenticationFilter.class);
         return http.build();

    }

    // making auth manager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception
    {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

}
