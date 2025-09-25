package com.Proyecto.Chifa.Modelo;

import com.Proyecto.Chifa.Modelo.Local.EstadoLocal;
import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

public class LocalDto {
    
    @NotEmpty(message = "Ingrese el nombre del local")
    private String nombre;
    
    @NotEmpty(message = "Ingrese la ubicación del local")
    private String ubicacion;
    
    @NotEmpty(message = "Ingrese el horario del local")
    private String horario;
    
    @NotNull(message = "Ingrese el estado del local")
    private EstadoLocal estado;
    
    
    @NotNull(message = "Cargue una imagen")
    private MultipartFile imagen;

    public LocalDto() {
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public EstadoLocal getEstado() {
        return estado;
    }

    public void setEstado(EstadoLocal estado) {
        this.estado = estado;
    }

    public MultipartFile getImagen() {
        return imagen;
    }

    public void setImagen(MultipartFile imagen) {
        this.imagen = imagen;
    }
    
    
    
}
