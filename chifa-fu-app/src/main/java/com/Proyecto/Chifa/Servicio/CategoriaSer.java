package com.Proyecto.Chifa.Servicio;

import org.springframework.beans.factory.annotation.Autowired;
import com.Proyecto.Chifa.Repositorio.CategoriaRepo;
import org.springframework.stereotype.Service;
import com.Proyecto.Chifa.Modelo.Categoria;
import java.util.Optional;
import java.util.List;

@Service
public class CategoriaSer {

    @Autowired
    private CategoriaRepo repo;

    //Listar todas las categorias
    public List<Categoria> listarCategorias() {
        return repo.findAll();
    }

    //Guardar una nueva categoria en la base de datos
    public Categoria guardar(Categoria categoria) {
        return repo.save(categoria);
    }

    //Obtener una categoria por id
    public Optional<Categoria> obtenerPorId(Integer id) {
        return repo.findById(id);
    }

    //Eliminar una categoria de la bd
    public void eliminar(Integer id) {
        repo.deleteById(id);
    }

}
