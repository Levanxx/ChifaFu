package com.Proyecto.Chifa.Seguridad;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.ServletException;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements org.springframework.security.web.authentication.AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        var authorities = authentication.getAuthorities();

        if (authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            response.sendRedirect("/admin/inicio");
        } else if (authorities.contains(new SimpleGrantedAuthority("ROLE_CLIENTE"))) {
            response.sendRedirect("/perfil");
        } else {
            response.sendRedirect("/inicio");
        }
    }

}
