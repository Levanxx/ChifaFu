package com.Proyecto.Chifa.Controlador;

import org.springframework.beans.factory.annotation.Autowired;
import com.Proyecto.Chifa.Modelo.Local.EstadoLocal;
import com.Proyecto.Chifa.Repositorio.LocalRepo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.Proyecto.Chifa.Servicio.LocalSer;
import org.springframework.ui.Model;
import com.Proyecto.Chifa.Modelo.*;
import org.springframework.http.*;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
public class LocalControl {

    @Autowired
    private LocalSer localSer;

    @Autowired
    private LocalRepo localRepo;

    //Mostrar página de locales en el administrador
    @GetMapping("/admin/locales")
    public String mostrarFormulario(Model model) {
        List<Local> locales = localSer.listarLocales();
        model.addAttribute("locales", locales);
        model.addAttribute("localDto", new LocalDto());
        model.addAttribute("estados", EstadoLocal.values());
        return "/admin/locales";
    }

    //Guardar un nuevo local en la base de datos
    @PostMapping("/admin/locales/guardar")
    public String guardarLocal(@Valid @ModelAttribute LocalDto localDto) throws IOException {
        localSer.guardarLocal(localDto);
        return "redirect:/admin/locales";
    }

    //Mostrar la imagen de los locales
    @GetMapping("/locales/imagen/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> verImagen(@PathVariable Integer id) {
        Local local = localRepo.findById(id).orElse(null);
        if (local == null || local.getImagen() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/png")
                .body(local.getImagen());
    }

    //Ver locales en la pagina de clientes
    @GetMapping("/locales")
    public String locales(Model model) {
        List<Local> locales = localSer.listarLocales();
        model.addAttribute("locales", locales);
        return "locales";
    }

    //Eliminar local de la base de datos
    @GetMapping("/admin/locales/eliminar")
    public String eliminarLocales(@RequestParam int idLocal) {
        localSer.eliminarLocal(idLocal);
        return "redirect:/admin/locales";
    }

}
