package com.Proyecto.Chifa.Seguridad;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Bean;

@Configuration
@EnableWebSecurity
public class ConfiguracionSeguridad {

    private final CustomAuthenticationSuccessHandler successHandler;

    public ConfiguracionSeguridad(CustomAuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/inicio",
                        "/locales",
                        "/locales/imagen/**",
                        "/login",
                        "/logout",
                        "/registro",
                        "/menu",
                        "/menu/imagen/**",
                        "/menu/**",
                        "/**.css", "/**.png", "/**.jpg", "/**.webp")
                .permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/perfil").hasRole("CLIENTE")
                .anyRequest().authenticated()
        )
                .formLogin(form -> form
                .loginPage("/login")
                .usernameParameter("email")
                .passwordParameter("contrasena")
                .successHandler(successHandler)
                )
                .logout(config -> config.logoutSuccessUrl("/"))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
