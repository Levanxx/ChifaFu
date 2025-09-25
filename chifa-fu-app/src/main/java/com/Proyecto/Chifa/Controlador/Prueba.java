package com.Proyecto.Chifa.Controlador;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;

@Controller
public class Prueba {
    
    @GetMapping("/inicio")
    public String inicio() {
        return "inicio";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/admin/inicio")
    public String inicioAdministrador() {
        return "/admin/inicio";
    }
    
}
