package com.Proyecto.Chifa.Repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Proyecto.Chifa.Modelo.Usuario;
import java.util.Optional;
import java.util.List;

public interface UsuarioRepo  extends JpaRepository<Usuario, Integer> {
    
    public Usuario findByEmail(String email);
    List<Usuario> findByRol(String rol);
    Optional<Usuario> findByDni(String dni);
    
}
