package com.Proyecto.Chifa.Repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Proyecto.Chifa.Modelo.Pedido;

import java.util.List;
import java.util.Optional;

public interface PedidoRepo extends JpaRepository<Pedido, Integer> {
    List<Pedido> findByUsuarioIdUsuario(Integer idUsuario);

    Optional<Pedido> findById(Integer id);

}
