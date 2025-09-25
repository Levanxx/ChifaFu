package com.Proyecto.Chifa.Modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "detallePedido")
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDetallePedido;
    @ManyToOne
    @JoinColumn(name = "idPedido")
    private Pedido pedido;
    @ManyToOne
    @JoinColumn(name = "idMenu")
    private Menu menu;
    private Integer cantidad;
    private Double precioUnitario;

    public DetallePedido() {
    }

    public Integer getIdDetallePedido() {
        return idDetallePedido;
    }

    public void setIdDetallePedido(Integer idDetallePedido) {
        this.idDetallePedido = idDetallePedido;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
    
}
