package com.Proyecto.Chifa.Servicio;

import com.Proyecto.Chifa.Modelo.*;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface CarritoSer {

    void agregarAlCarrito(Usuario usuario, Menu menu, Integer cantidad);
    List<Carrito> obtenerCarritoPorUsuario(Usuario usuario);
    @Transactional
    void eliminarCarritoPorUsuario(Usuario usuario);

}
