package com.equipoC.alquilerQuinchos.servicios;

import com.equipoC.alquilerQuinchos.entidades.Calendario;
import com.equipoC.alquilerQuinchos.entidades.Imagen;
import com.equipoC.alquilerQuinchos.entidades.Inmueble;
import com.equipoC.alquilerQuinchos.entidades.Usuario;
import com.equipoC.alquilerQuinchos.excepciones.MiException;
import com.equipoC.alquilerQuinchos.repositorios.InmuebleRepositorio;
import com.equipoC.alquilerQuinchos.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class InmuebleServicio {

    @Autowired
    private InmuebleRepositorio inmuebleRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private CalendarioServicio calendarioServicio;

    @Transactional
    public void crearInmueble(String descripcion, String nombre, String ubicacion, Boolean cochera, Boolean parrilla,
            Boolean pileta, Double precioBase, Double precioTotal, List<MultipartFile> imgProp,
            String idPropietario) throws MiException {

        validarInmueble(descripcion, nombre, ubicacion, precioBase, imgProp);

        if (parrilla == null) {
            parrilla = false;
        }

        if (cochera == null) {
            cochera = false;
        }

        if (pileta == null) {
            pileta = false;
        }

        Inmueble inmueble = new Inmueble();
        inmueble.setNombre(nombre);
        inmueble.setUbicacion(ubicacion);
        inmueble.setCochera(cochera);
        inmueble.setParrilla(parrilla);
        inmueble.setPileta(pileta);
        inmueble.setPrecioBase(precioBase);
        inmueble.setDescripcion(descripcion);

        Calendario calendario = calendarioServicio.crearCalendario();
        inmueble.setCalendarioInmueble(calendario);
        inmueble.setPrecioTotal(precioTotal);
        Usuario usuario = usuarioRepositorio.buscarPorId(idPropietario);
        inmueble.setUserProp(usuario);
        List<Imagen> imagenes = new ArrayList<>();

        for (MultipartFile archivoImagen : imgProp) {
            Imagen imagen = imagenServicio.guardar(archivoImagen);
            imagenes.add(imagen);
        }

        inmueble.setImagenInmueble(imagenes);
        inmuebleRepositorio.save(inmueble);

        for (Imagen img : imagenes) {
            img.setInmueble(inmueble);
        }
    }

    @Transactional
    public Inmueble modificarInmueble(String descripcion, Long id, String nombre, String ubicacion, Boolean cochera,
            Boolean parrilla, Boolean pileta,
            Double precioBase, Double precioTotal, List<MultipartFile> archivosImagenes) throws MiException {

        validarInmueble(descripcion, nombre, ubicacion, precioBase, archivosImagenes);
        Inmueble inmueble = inmuebleRepositorio.findById(id).orElse(null);

        if (inmueble == null) {
            throw new MiException("El inmueble no se encuentra.");
        }

        if (cochera == null) {
            cochera = false;
        }

        if (parrilla == null) {
            parrilla = false;
        }

        if (pileta == null) {
            pileta = false;
        }

        inmueble.setNombre(nombre);
        inmueble.setUbicacion(ubicacion);
        inmueble.setCochera(cochera);
        inmueble.setParrilla(parrilla);
        inmueble.setPileta(pileta);
        inmueble.setPrecioBase(precioBase);
        inmueble.setPrecioTotal(precioTotal);
        inmueble.setDescripcion(descripcion);

        List<Imagen> nuevasImagenes = new ArrayList<>();

        if (archivosImagenes.isEmpty()) {
            return inmuebleRepositorio.save(inmueble);

        } else {
            for (MultipartFile archivoImagen : archivosImagenes) {
                Imagen imagen = imagenServicio.guardar(archivoImagen);
                nuevasImagenes.add(imagen);
                imagen.setInmueble(inmueble);
            }

            inmueble.getImagenInmueble().addAll(nuevasImagenes);

            return inmuebleRepositorio.save(inmueble);
        }

    }

    @Transactional
    public void eliminarInmueble(Long id) throws MiException {

        if (id == null) {
            throw new MiException("El inmueble no se encuentra.");
        }

        inmuebleRepositorio.deleteById(id);
    }

    @Transactional
    public void eliminarInmueblesPorPropietarioId(String propietarioId) {
        @SuppressWarnings("deprecation")
        Usuario propietario = usuarioRepositorio.getOne(propietarioId);
        List<Inmueble> inmuebles = inmuebleRepositorio.findByUserProp(propietario);

        for (Inmueble inmueble : inmuebles) {
            inmuebleRepositorio.delete(inmueble);
        }
    }

    @SuppressWarnings("deprecation")
    public Inmueble getOne(Long id) {
        return inmuebleRepositorio.getOne(id);
    }

    public List<Inmueble> listarInmueblesUsuario(String id) {
        return inmuebleRepositorio.buscarPorUserId(id);
    }

    @Transactional
    public List<Inmueble> listarTodosLosInmuebles() {
        return inmuebleRepositorio.findAll();
    }

    @Transactional
    public List<Inmueble> listarInmueblesPorBusquedaPersonalizada(String search, String pileta, String parrilla,
            String cochera) {

        System.out.println(search);

        if (validarParametros(search, pileta, parrilla, cochera) == 4
                || validarParametros(search, pileta, parrilla, cochera) == 3) {
            search = null;
        }

        System.out.println(search);

        if (search != null && pileta.equalsIgnoreCase("siPileta") && parrilla.equalsIgnoreCase("siParrilla")
                && cochera.equalsIgnoreCase("siCochera")) {
            return inmuebleRepositorio.findAllTodos(search);

        } else if (search != null && pileta.equalsIgnoreCase("siPileta") && parrilla.equalsIgnoreCase("siParrilla")) {
            return inmuebleRepositorio.findAllPileParri(search);

        } else if (search != null && pileta.equalsIgnoreCase("siPileta") && cochera.equalsIgnoreCase("siCochera")) {
            return inmuebleRepositorio.findAllPileCoche(search);

        } else if (search != null && parrilla.equalsIgnoreCase("siParrilla") && cochera.equalsIgnoreCase("siCochera")) {
            return inmuebleRepositorio.findAllParriCoche(search);

        } else if (search != null && parrilla.equalsIgnoreCase("siParrilla")) {
            return inmuebleRepositorio.findAllParri(search);

        } else if (search != null && pileta.equalsIgnoreCase("siPileta")) {
            return inmuebleRepositorio.findAllPile(search);

        } else if (search != null && cochera.equalsIgnoreCase("siCochera")) {
            return inmuebleRepositorio.findAllCoche(search);

        } else if (search != null && pileta.equalsIgnoreCase("falso") && parrilla.equalsIgnoreCase("falso")
                && cochera.equalsIgnoreCase("falso")) {
            return inmuebleRepositorio.findAllSearch(search);

        }
        if (search == null && pileta.equalsIgnoreCase("falso") && parrilla.equalsIgnoreCase("falso")
                && cochera.equalsIgnoreCase("falso")) {
            return inmuebleRepositorio.findAll();

        } else if (search == null && pileta.equalsIgnoreCase("siPileta") && parrilla.equalsIgnoreCase("siParrilla")
                && cochera.equalsIgnoreCase("siCochera")) {
            return inmuebleRepositorio.findAllTodosNull(search);

        } else if (search == null && pileta.equalsIgnoreCase("siPileta") && parrilla.equalsIgnoreCase("siParrilla")) {
            return inmuebleRepositorio.findAllPileParriNull(search);

        } else if (search == null && pileta.equalsIgnoreCase("siPileta") && cochera.equalsIgnoreCase("siCochera")) {
            return inmuebleRepositorio.findAllPileCocheNull(search);

        } else if (search == null && parrilla.equalsIgnoreCase("siParrilla") && cochera.equalsIgnoreCase("siCochera")) {
            return inmuebleRepositorio.findAllParriCocheNull(search);

        } else if (search == null && parrilla.equalsIgnoreCase("siParrilla")) {
            return inmuebleRepositorio.findAllParriNull(search);

        } else if (search == null && pileta.equalsIgnoreCase("siPileta")) {
            return inmuebleRepositorio.findAllPileNull(search);

        } else if (search == null && cochera.equalsIgnoreCase("siCochera")) {
            return inmuebleRepositorio.findAllCocheNull(search);

        }

        return inmuebleRepositorio.findAll();
    }

    public boolean validarInmueble(String descripcion, String nombre, String ubicacion, Double precioBase,
            List<MultipartFile> archivos) throws MiException {

        if (descripcion.isEmpty() || descripcion == null) {
            throw new MiException("Debe realizar una breve descripción del inmueble");
        }

        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("El nombre de usuario no puede ser nulo o estar vacío");
        }

        if (ubicacion.isEmpty() || ubicacion == null) {
            throw new MiException("El nombre de usuario no puede ser nulo o estar vacío");
        }

        if (precioBase == null || precioBase < 0) {
            throw new MiException("El precio base no es válido.");
        }

        if (archivos.isEmpty() == true) {
            throw new MiException("Debe adjuntar al menos una imagen del inmueble.");
        }

        return true;
    }

    public int validarParametros(String search, String pileta, String parrilla, String cochera) {

        int count = 0;

        if (pileta.length() > 0) {
            count += 1;
        }

        if (parrilla.length() > 0) {
            count += 1;
        }

        if (cochera.length() > 0) {
            count += 1;
        }

        if (search.equalsIgnoreCase("aarmppfcjulio2023")) {
            count += 1;
        } else if (search.length() > 0) {
            count = 5;
        }

        return count;
    }
}
