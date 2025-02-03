package com.ditossystem.ditos.api_security.configuration;

import com.ditossystem.ditos.api_security.security.AuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                // Permite acesso ao GET /coupons e /notification para qualquer usuário
                .requestMatchers(HttpMethod.GET, "/coupons").permitAll()
                .requestMatchers(HttpMethod.GET, "/notifications").permitAll()
                // Qualquer outra requisição precisa ser autenticada
                .requestMatchers("/**").hasAuthority("desktop"))
            .httpBasic(Customizer.withDefaults())
            .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(new AuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
