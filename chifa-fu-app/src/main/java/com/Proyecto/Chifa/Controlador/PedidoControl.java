package com.Proyecto.Chifa.Controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.Proyecto.Chifa.Modelo.*;
import com.Proyecto.Chifa.Servicio.*;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class PedidoControl {

    @Autowired
    private UsuarioSer servUsuario;

    @Autowired
    private CarritoSer servCarrito;

    @Autowired
    private PedidoSer servPedido;

    @Autowired
    private PedidoSerImpl servPedidoImpl;

    //Confirmar un pedido
    @PostMapping("/pedido/confirmar")
    public String confirmarPedido(Principal principal,
            @RequestParam("direccionEntrega") String direccionEntrega,
            @RequestParam("tipoEntrega") Pedido.TipoEntrega tipoEntrega) {
        if (principal == null) {
            return "redirect:/login";
        }

        String email = principal.getName();
        Usuario usuario = servUsuario.buscarPorEmail(email);
        List<Carrito> itemsCarrito = servCarrito.obtenerCarritoPorUsuario(usuario);

        if (itemsCarrito.isEmpty()) {
            return "redirect:/carrito?error=CarritoVacio";
        }

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setEstado(Pedido.EstadoPedido.EN_ESPERA);
        pedido.setTipoEntrega(tipoEntrega);
        pedido.setDireccionEntrega(direccionEntrega);

        double total = itemsCarrito.stream()
                .mapToDouble(item -> item.getMenu().getPrecio() * item.getCantidad())
                .sum();
        pedido.setTotal(total);

        for (Carrito item : itemsCarrito) {
            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setMenu(item.getMenu());
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(item.getMenu().getPrecio());
            pedido.getDetalles().add(detalle);
        }

        servPedido.guardarPedido(pedido);
        servCarrito.eliminarCarritoPorUsuario(usuario);

        return "redirect:/pedidoExito";
    }

    //Mostrar pagina de exito pedido
    @GetMapping("/pedidoExito")
    public String mostrarExito() {
        return "pedidoExito";
    }

    //Listar todos los pedidos en la pagina administrador
    @GetMapping("/admin/pedidos")
    public String listarPedidos(Model model) {
        List<Pedido> pedidos = servPedido.obtenerTodos();
        model.addAttribute("pedidos", pedidos);
        return "admin/pedidos";
    }

    //Generar bolera del pedido
    @GetMapping("/pedido/{id}/boleta")
    public void descargarBoleta(@PathVariable Integer id, HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=boleta-" + id + ".pdf");
        servPedidoImpl.generarBoletaPDF(id, response);
    }

}
