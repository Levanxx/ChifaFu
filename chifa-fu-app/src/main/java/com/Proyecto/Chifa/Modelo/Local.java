package com.Proyecto.Chifa.Modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "local")
public class Local {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idLocal;
    private String nombre;
    private String ubicacion;
    private String horario;
    @Enumerated(EnumType.STRING)
    private EstadoLocal estado;
    @Lob
    @Column(name = "imagen", columnDefinition = "LONGBLOB")
    private byte[] imagen;
    private String nombreImagen;
    
    public Local() {
    }
    
    public Integer getIdLocal() {
        return idLocal;
    }

    public void setIdLocal(Integer idLocal) {
        this.idLocal = idLocal;
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

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getNombreImagen() {
        return nombreImagen;
    }

    public void setNombreImagen(String nombreImagen) {
        this.nombreImagen = nombreImagen;
    }
    
    public enum EstadoLocal {
        ABIERTO,
        CERRADO,
        EN_MANTENIMIENTO
    }
    
}
