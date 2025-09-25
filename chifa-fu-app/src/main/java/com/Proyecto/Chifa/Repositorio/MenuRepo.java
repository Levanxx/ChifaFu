package com.Proyecto.Chifa.Repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Proyecto.Chifa.Modelo.Menu;
import java.util.List;

public interface MenuRepo extends JpaRepository<Menu, Integer> {

    List<Menu> findByCategoria_IdCategoria(Integer idCategoria);
    
}
