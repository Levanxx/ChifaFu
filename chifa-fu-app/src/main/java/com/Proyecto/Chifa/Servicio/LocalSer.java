package com.Proyecto.Chifa.Servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import com.Proyecto.Chifa.Repositorio.LocalRepo;
import org.springframework.stereotype.Service;
import com.Proyecto.Chifa.Modelo.LocalDto;
import com.Proyecto.Chifa.Modelo.Local;
import java.io.IOException;
import java.util.List;

@Service
public class LocalSer {

    @Autowired
    private LocalRepo localRepo;

    //Guardar un local en la bd
    public void guardarLocal(LocalDto dto) throws IOException {
        Local local = new Local();
        local.setNombre(dto.getNombre());
        local.setUbicacion(dto.getUbicacion());
        local.setHorario(dto.getHorario());
        local.setEstado(dto.getEstado());
        MultipartFile imagen = dto.getImagen();
        if (imagen != null && !imagen.isEmpty()) {
            local.setImagen(imagen.getBytes());
            local.setNombreImagen(imagen.getOriginalFilename());
        }
        localRepo.save(local);
    }

    //Listar todos los locales
    public List<Local> listarLocales() {
        return localRepo.findAll();
    }

    //Eliminar un local
    public void eliminarLocal(int idLocal) {
        localRepo.deleteById(idLocal);
    }

}
