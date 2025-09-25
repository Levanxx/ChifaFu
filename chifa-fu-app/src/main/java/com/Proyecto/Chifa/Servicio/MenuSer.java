package com.Proyecto.Chifa.Servicio;

import org.springframework.beans.factory.annotation.Autowired;
import com.Proyecto.Chifa.Repositorio.MenuRepo;
import org.springframework.stereotype.Service;
import com.Proyecto.Chifa.Modelo.Menu;
import java.util.Optional;
import java.util.List;

@Service
public class MenuSer {

    @Autowired
    private MenuRepo repo;

    //Listar todos los menus
    public List<Menu> listarMenus() {
        return repo.findAll();
    }

    //Guardar un nuevo menu
    public Menu guardar(Menu menu) {
        return repo.save(menu);
    }

    //Obtener un menu por su id
    public Optional<Menu> obtenerPorId(Integer id) {
        return repo.findById(id);
    }

    //Eliminar un medu de la base de datos
    public void eliminar(Integer id) {
        repo.deleteById(id);
    }

    //Listar los menus según su categoria
    public List<Menu> listarPorCategoria(Integer idCategoria) {
        return repo.findByCategoria_IdCategoria(idCategoria);
    }

}
