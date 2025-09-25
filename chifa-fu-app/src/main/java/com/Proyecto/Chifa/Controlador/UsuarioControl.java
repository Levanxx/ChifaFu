package com.Proyecto.Chifa.Controlador;

import com.Proyecto.Chifa.Repositorio.PedidoRepo;
import com.Proyecto.Chifa.Servicio.PedidoSer;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import com.Proyecto.Chifa.Repositorio.UsuarioRepo;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import com.Proyecto.Chifa.Servicio.UsuarioSer;
import org.springframework.ui.Model;
import com.Proyecto.Chifa.Modelo.*;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.Optional;
import java.util.List;
import java.util.Map;

@Controller
public class UsuarioControl {

    @Autowired
    private UsuarioRepo repo;

    @Autowired
    private UsuarioSer ser;

    @Autowired
    private PedidoRepo pedidoRepo;

    @Autowired
    private PedidoSer pedidoSer;

    //Mostrat la página de perfil de usuario
    @GetMapping("/perfil")
    public String perfil(Authentication auth, Model model) {
        Usuario usuario = repo.findByEmail(auth.getName());
        model.addAttribute("usuario", usuario);
        return "perfil";
    }

    //Mostrar el formulario de registro
    @GetMapping("/registro")
    public String registro(Model model) {
        RegistroDto registroDto = new RegistroDto();
        model.addAttribute(registroDto);
        model.addAttribute("success", false);
        return "registro";
    }

    //Agregar un nuevo usuario a la base de datos
    @PostMapping("/registro")
    public String registro(Model model, @Valid @ModelAttribute RegistroDto registroDto, BindingResult result) {
        if (!registroDto.getContrasena().equals(registroDto.getConfirmarContrasena())) {
            result.addError(new FieldError("registroDto", "confirmarContrasena", "Las contraseñas no son iguales"));
        }
        Usuario usuario = repo.findByEmail(registroDto.getEmail());
        if (usuario != null) {
            result.addError(new FieldError("registroDto", "email", "El correo ya está en uso"));
        }
        if (result.hasErrors()) {
            return "registro";
        }
        try {
            var bCryptEncoder = new BCryptPasswordEncoder();
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombre(registroDto.getNombre());
            nuevoUsuario.setApellido(registroDto.getApellido());
            nuevoUsuario.setDni(registroDto.getDni());
            nuevoUsuario.setTelefono(registroDto.getTelefono());
            nuevoUsuario.setDireccionPredeterminada(registroDto.getDireccionPredeterminada());
            nuevoUsuario.setEmail(registroDto.getEmail());
            nuevoUsuario.setRol(registroDto.getRol());
            nuevoUsuario.setContrasena(bCryptEncoder.encode(registroDto.getContrasena()));
            repo.save(nuevoUsuario);
            model.addAttribute("registroDto", new RegistroDto());
            model.addAttribute("success", true);
        } catch (Exception ex) {
            result.addError(new FieldError("registroDto", "nombre", ex.getMessage()));
        }
        return "registro";
    }

    //Actulizar correo electronico del usuario en la base de datos
    @PostMapping("/actualizarCorreo")
    public String actualizarCorreo(
            @RequestParam String dni,
            @RequestParam String email,
            RedirectAttributes redirectAttributes
    ) {
        Optional<Usuario> usuarioOpt = repo.findByDni(dni);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setEmail(email);
            repo.save(usuario);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    usuario,
                    usuario.getContrasena()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            redirectAttributes.addFlashAttribute("mensaje", "Correo actualizado correctamente");
        }
        return "redirect:/perfil";
    }

    //Actualizar el telefono del usuario en la base de datos
    @PostMapping("/actualizarTelefono")
    public String actualizarTelefono(@RequestParam String dni, @RequestParam String telefono, RedirectAttributes redirectAttributes) {
        Optional<Usuario> usuarioOpt = repo.findByDni(dni);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setTelefono(telefono);
            repo.save(usuario);
            redirectAttributes.addFlashAttribute("mensaje", "Teléfono actualizado correctamente");
        }
        return "redirect:/perfil";
    }

    //Actualizar la direccion del usuario en la base de datos
    @PostMapping("/actualizarDireccion")
    public String actualizarDireccion(@RequestParam String dni, @RequestParam String direccionPredeterminada, RedirectAttributes redirectAttributes) {
        Optional<Usuario> usuarioOpt = repo.findByDni(dni);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setDireccionPredeterminada(direccionPredeterminada);
            repo.save(usuario);
            redirectAttributes.addFlashAttribute("mensaje", "Dirección actualizada correctamente");
        }
        return "redirect:/perfil";
    }

    //Mostrar clientes en la pagina administrador
    @GetMapping("/admin/clientes")
    public String ListarClientes(Model modelo) {
        List<Usuario> clientes = ser.listarPorRol("CLIENTE");

        Map<Integer, List<Pedido>> pedidosPorCliente = new HashMap<>();
        for (Usuario cliente : clientes) {
            List<Pedido> pedidos = pedidoSer.obtenerPedidosPorUsuario(cliente.getIdUsuario());
            pedidosPorCliente.put(cliente.getIdUsuario(), pedidos);
        }

        modelo.addAttribute("clientes", clientes);
        modelo.addAttribute("pedidosPorCliente", pedidosPorCliente);

        return "admin/clientes";
    }

    //mostrar pedidos por cliente
    @GetMapping("/admin/clientes/pedidos")
    public String obtenerPedidosCliente(@RequestParam("idUsuario") Integer idUsuario, Model model) {
        List<Pedido> pedidos = pedidoSer.obtenerPedidosPorUsuario(idUsuario);
        model.addAttribute("pedidos", pedidos);
        return "admin/fragments/pedidos_cliente :: pedidosModal";
    }

    //Eliminar un usuario desde la pagina de administrador
    @GetMapping("/admin/clientes/eliminar")
    public String eliminarClientes(@RequestParam int idUsuario) {
        ser.eliminarUsuario(idUsuario);
        return "redirect:/admin/clientes";
    }

    //Mostrar el historial de pedido en el perfil
    @GetMapping("/perfil/historial")
    public String verHistorial(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName(); 
        Usuario usuario = ser.buscarPorEmail(username); 
        if (usuario == null) {
            return "redirect:/login";
        }
        List<Pedido> pedidos = pedidoSer.obtenerPedidosPorUsuario(usuario.getIdUsuario());
        model.addAttribute("pedidos", pedidos);
        return "historial_pedidos";
    }

    //Cambiar estado de un pedido
    @PostMapping("/admin/pedido/cambiarEstado")
    public String cambiarEstadoPedido(@RequestParam("idPedido") Integer idPedido,
            @RequestParam("estado") String nuevoEstado) {
        Optional<Pedido> optionalPedido = pedidoRepo.findById(idPedido);
        if (optionalPedido.isPresent()) {
            Pedido pedido = optionalPedido.get();
            pedido.setEstado(Pedido.EstadoPedido.valueOf(nuevoEstado)); 
            pedidoRepo.save(pedido);
            return "redirect:/admin/pedidos"; 
        }
        return "redirect:/admin/pedidos?error";
    }
    
    //MOstrar el formulario de registro para admnistradores y la lista de todos
    @GetMapping("/admin/administradores")
    public String registrarAdministrador(Model model) {
        List<Usuario> administradores = ser.listarPorRol("ADMIN");
        model.addAttribute("administradores", administradores);
        RegistroDto registroDto = new RegistroDto();
        model.addAttribute(registroDto);
        model.addAttribute("success", false);
        return "admin/administradores";
    }

    
    //Crear un nuevo administrador
    @PostMapping("/admin/administradores")
    public String nuevoAdministrador(Model model, @Valid @ModelAttribute RegistroDto registroDto, BindingResult result) {
        if (!registroDto.getContrasena().equals(registroDto.getConfirmarContrasena())) {
            result.addError(new FieldError("registroDto", "confirmarContrasena", "Las contraseñas no son iguales"));
        }
        Usuario usuario = repo.findByEmail(registroDto.getEmail());
        if (usuario != null) {
            result.addError(new FieldError("registroDto", "email", "El correo ya está en uso"));
        }
        if (result.hasErrors()) {
            return "admin/administradores";
        }
        try {
            var bCryptEncoder = new BCryptPasswordEncoder();
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombre(registroDto.getNombre());
            nuevoUsuario.setApellido(registroDto.getApellido());
            nuevoUsuario.setDni(registroDto.getDni());
            nuevoUsuario.setTelefono(registroDto.getTelefono());
            nuevoUsuario.setDireccionPredeterminada(registroDto.getDireccionPredeterminada());
            nuevoUsuario.setEmail(registroDto.getEmail());
            nuevoUsuario.setRol(registroDto.getRol());
            nuevoUsuario.setContrasena(bCryptEncoder.encode(registroDto.getContrasena()));
            repo.save(nuevoUsuario);
            model.addAttribute("registroDto", new RegistroDto());
            model.addAttribute("success", true);
        } catch (Exception ex) {
            result.addError(new FieldError("registroDto", "nombre", ex.getMessage()));
        }
        return "admin/administradores";
    }
    
    
    
    

}
