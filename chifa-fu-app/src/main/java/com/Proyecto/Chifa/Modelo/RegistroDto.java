package com.Proyecto.Chifa.Modelo;

import jakarta.validation.constraints.*;

public class RegistroDto {
    
    @NotEmpty(message = "Ingrese su nombre")
    private String nombre;
    
    @NotEmpty(message = "Ingrese su apellido")
    private String apellido;
    
    @NotEmpty(message = "Ingrese su DNI")
    @Size(max= 8)
    private String dni;
    
    @NotEmpty(message = "Ingrese su telefono")
    @Size(max=9)
    private String telefono;
    
    @NotEmpty(message = "Ingrese su dirección")
    private String direccionPredeterminada;
    
    @Email
    @NotEmpty(message = "Ingrese su dirección de correo electrónico")
    private String email;
    
    @Size(min=8, message = "La contraseña debe tener al menos 8 caracteres")
    private String contrasena;
    
    private String confirmarContrasena;
    
    private String rol;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccionPredeterminada() {
        return direccionPredeterminada;
    }

    public void setDireccionPredeterminada(String direccionPredeterminada) {
        this.direccionPredeterminada = direccionPredeterminada;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getConfirmarContrasena() {
        return confirmarContrasena;
    }

    public void setConfirmarContrasena(String confirmarContrasena) {
        this.confirmarContrasena = confirmarContrasena;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
    
    
    
}
