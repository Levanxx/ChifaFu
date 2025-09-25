package com.Proyecto.Chifa.Controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;
import com.Proyecto.Chifa.Servicio.CategoriaSer;
import org.springframework.stereotype.Controller;
import org.springframework.http.ResponseEntity;
import com.Proyecto.Chifa.Servicio.MenuSer;
import org.springframework.http.HttpHeaders;
import org.springframework.ui.Model;
import com.Proyecto.Chifa.Modelo.*;
import java.io.IOException;

@Controller
public class MenuControl {

    @Autowired
    private CategoriaSer servCategoria;

    @Autowired
    private MenuSer servMenu;

    //Mostrar la pagina de menu
    @GetMapping("/admin/menu")
    public String paginaMenu(Model model) {
        model.addAttribute("categorias", servCategoria.listarCategorias());
        model.addAttribute("menus", servMenu.listarMenus());
        model.addAttribute("nuevaCategoria", new Categoria());
        model.addAttribute("nuevoMenu", new Menu());
        return "admin/menu";
    }

    //Guardar una nueva categoria
    @PostMapping("/categorias/guardar")
    public String guardarCategoria(@ModelAttribute("nuevaCategoria") Categoria categoria) {
        servCategoria.guardar(categoria);
        return "redirect:/admin/menu";
    }

    //Guardar un nuevo menu
    @PostMapping("/menu/guardar")
    public String guardarMenu(@ModelAttribute("nuevoMenu") Menu menu, @RequestParam("fileImagen") MultipartFile fileImagen) {
        try {
            if (!fileImagen.isEmpty()) {
                menu.setImagen(fileImagen.getBytes());
                menu.setNombreImagen(fileImagen.getOriginalFilename());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        servMenu.guardar(menu);
        return "redirect:/admin/menu";
    }

    //Eliminar un menu desde la pagina administrador
    @GetMapping("/admin/menu/eliminar")
    public String eliminarMenu(@RequestParam int idMenu) {
        servMenu.eliminar(idMenu);
        return "redirect:/admin/menu";
    }

    //Mostar pagina de menu
    @GetMapping("/menu")
    public String mostrarMenu(@RequestParam(name = "categoriaId", required = false) Integer categoriaId, Model model) {
        model.addAttribute("categorias", servCategoria.listarCategorias());

        if (categoriaId != null) {
            model.addAttribute("menus", servMenu.listarPorCategoria(categoriaId));
        } else {
            model.addAttribute("menus", servMenu.listarMenus());
        }

        model.addAttribute("categoriaId", categoriaId); 
        return "/menu"; 
    }

    //Mostrar la imagen de cada plato
    @GetMapping("/menu/imagen/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> verImagen(@PathVariable Integer id) {
        Menu menu = servMenu.obtenerPorId(id).orElse(null);
        if (menu == null || menu.getImagen() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/png")
                .body(menu.getImagen());
    }

}
