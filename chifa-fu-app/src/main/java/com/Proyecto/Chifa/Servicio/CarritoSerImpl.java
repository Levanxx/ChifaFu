package com.Proyecto.Chifa.Servicio;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.Proyecto.Chifa.Repositorio.CarritoRepo;
import org.springframework.stereotype.Service;
import com.Proyecto.Chifa.Modelo.*;
import java.util.List;

@Service
public class CarritoSerImpl implements CarritoSer {

    @Autowired
    private CarritoRepo repo;

    //Agregar un menu al carrito
    @Override
    public void agregarAlCarrito(Usuario usuario, Menu menu, Integer cantidad) {
        List<Carrito> items = repo.findByUsuario(usuario);
        for (Carrito item : items) {
            if (item.getMenu().getIdMenu().equals(menu.getIdMenu())) {
                item.setCantidad(item.getCantidad() + cantidad);
                repo.save(item);
                return;
            }
        }
        Carrito nuevo = new Carrito();
        nuevo.setUsuario(usuario);
        nuevo.setMenu(menu);
        nuevo.setCantidad(cantidad);
        repo.save(nuevo);
    }

    //Obtener el carrito del usuario
    @Override
    public List<Carrito> obtenerCarritoPorUsuario(Usuario usuario) {
        return repo.findByUsuario(usuario);
    }

    //Vaciar el carrito del usuario
    @Override
    @Transactional
    public void eliminarCarritoPorUsuario(Usuario usuario) {
        repo.deleteByUsuario(usuario);
    }

}
