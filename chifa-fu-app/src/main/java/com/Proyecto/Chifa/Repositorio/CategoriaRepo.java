package com.Proyecto.Chifa.Repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Proyecto.Chifa.Modelo.Categoria;

public interface CategoriaRepo extends JpaRepository<Categoria, Integer> {
    
}
