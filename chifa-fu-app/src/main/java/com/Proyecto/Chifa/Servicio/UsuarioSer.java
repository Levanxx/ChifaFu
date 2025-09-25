package com.Proyecto.Chifa.Servicio;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import com.Proyecto.Chifa.Repositorio.UsuarioRepo;
import org.springframework.stereotype.Service;
import com.Proyecto.Chifa.Modelo.Usuario;
import java.util.Optional;
import java.util.List;

@Service
public class UsuarioSer implements UserDetailsService {

    @Autowired
    private UsuarioRepo repo;

    //Buscar un usuario por email
    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        Usuario usuario = repo.findByEmail(email);
        if (usuario != null) {
            var springUser = User.withUsername(usuario.getEmail())
                    .password(usuario.getContrasena())
                    .roles(usuario.getRol().toUpperCase())
                    .build();
            return springUser;
        }
        return null;
    }

    //Listar los usuarios por rol
    public List<Usuario> listarPorRol(String rol) {
        return repo.findByRol(rol);
    }

    //listar todos los usuarios
    public List<Usuario> listarTodos() {
        return repo.findAll();
    }

    //buscar por id
    public Optional<Usuario> buscarPorIdi(int idUsuario) {
        return repo.findById(idUsuario);
    }
    
    //Buscar un usuario por email
    public Usuario buscarPorEmail(String email) {
        return repo.findByEmail(email);
    }

    //Guardar un nuevo usuario en la bd
    public Usuario guardarUsuario(Usuario usuario) {
        return repo.save(usuario);
    }

    //Eliminar un usuario de la bd
    public void eliminarUsuario(int idUsuario) {
        repo.deleteById(idUsuario);
    }

}
