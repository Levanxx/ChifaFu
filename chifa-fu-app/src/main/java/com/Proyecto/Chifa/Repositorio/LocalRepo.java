package com.Proyecto.Chifa.Repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Proyecto.Chifa.Modelo.Local;

public interface LocalRepo extends JpaRepository<Local, Integer> {
    
}
