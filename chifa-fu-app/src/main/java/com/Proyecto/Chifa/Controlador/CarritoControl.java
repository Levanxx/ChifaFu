package com.Proyecto.Chifa.Controlador;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import com.Proyecto.Chifa.Servicio.*;
import org.springframework.ui.Model;
import com.Proyecto.Chifa.Modelo.*;
import java.security.Principal;
import java.util.Optional;
import java.util.List;

@Controller
public class CarritoControl {

    @Autowired
    private CarritoSer servCarrito;

    @Autowired
    private MenuSer servMenu;

    @Autowired
    private UsuarioSer servUsuario;

    //Mostrar el carrito del usuario logueado
    @GetMapping("/carrito")
    public String verCarrito(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        String email = principal.getName();
        Usuario usuario = servUsuario.buscarPorEmail(email);
        model.addAttribute("usuario", usuario);

        List<Carrito> itemsCarrito = servCarrito.obtenerCarritoPorUsuario(usuario);
        double total = itemsCarrito.stream().mapToDouble(item -> item.getMenu().getPrecio() * item.getCantidad()).sum();

        model.addAttribute("itemsCarrito", servCarrito.obtenerCarritoPorUsuario(usuario));
        model.addAttribute("total", total);
        return "carrito";
    }

    //Agregar un menu al carrito del usuario logueado
    @GetMapping("/carrito/agregar")
    public String agregarAlCarrito(@RequestParam("idMenu") Integer idMenu,
            @RequestParam(value = "cantidad", defaultValue = "1") Integer cantidad, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        String email = principal.getName();
        Usuario usuario = servUsuario.buscarPorEmail(email);
        Optional<Menu> optionalMenu = servMenu.obtenerPorId(idMenu);
        Menu menu = optionalMenu.get();
        servCarrito.agregarAlCarrito(usuario, menu, cantidad);
        return "redirect:/carrito";
    }

}
