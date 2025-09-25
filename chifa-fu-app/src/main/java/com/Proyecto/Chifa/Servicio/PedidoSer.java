package com.Proyecto.Chifa.Servicio;

import com.Proyecto.Chifa.Modelo.Pedido;
import java.util.List;
import java.util.Optional;

public interface PedidoSer {
    
    void guardarPedido(Pedido pedido);
    List<Pedido> obtenerTodos();
    List<Pedido> obtenerPedidosPorUsuario(Integer idUsuario);
    Pedido obtenerPorId(Integer id);

}
