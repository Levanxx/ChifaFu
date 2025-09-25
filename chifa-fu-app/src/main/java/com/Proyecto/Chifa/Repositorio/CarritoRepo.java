package com.Proyecto.Chifa.Repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Proyecto.Chifa.Modelo.*;
import java.util.List;

public interface CarritoRepo extends JpaRepository<Carrito, Integer> {
    
    List<Carrito> findByUsuario(Usuario usuario);
    
    void deleteByUsuario(Usuario usuario);
    
}
