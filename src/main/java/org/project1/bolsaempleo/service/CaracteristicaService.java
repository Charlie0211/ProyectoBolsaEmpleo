package org.project1.bolsaempleo.service;

import org.project1.bolsaempleo.entity.Caracteristica;
import org.project1.bolsaempleo.repository.CaracteristicaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CaracteristicaService {

    private final CaracteristicaRepository caracteristicaRepository;

    public CaracteristicaService(CaracteristicaRepository caracteristicaRepository) {
        this.caracteristicaRepository = caracteristicaRepository;
    }

    /** Retorna todas las características raíz (categorías) */
    public List<Caracteristica> obtenerRaices() {
        return caracteristicaRepository.findByPadreIsNullOrderByNombreAsc();
    }

    /** Retorna todas las características (para el dropdown de padre) */
    public List<Caracteristica> obtenerTodas() {
        return caracteristicaRepository.findAllByOrderByNombreAsc();
    }

    /** Retorna una característica por ID */
    public Optional<Caracteristica> obtenerPorId(Long id) {
        return caracteristicaRepository.findById(id);
    }

    /**
     * Crea una nueva característica.
     * Solo el Administrador puede invocar esto (validado en el controlador).
     *
     * @param nombre   Nombre de la característica
     * @param padreId  ID del padre, o null si es raíz
     * @return la característica guardada
     * @throws IllegalArgumentException si el nombre ya existe bajo el mismo padre
     */
    @Transactional
    public Caracteristica crear(String nombre, Long padreId) {
        String nombreLimpio = nombre.trim();

        // Verificar duplicados
        boolean duplicado = (padreId == null)
                ? caracteristicaRepository.existsByNombreIgnoreCaseAndPadreIsNull(nombreLimpio)
                : caracteristicaRepository.existsByNombreIgnoreCaseAndPadreId(nombreLimpio, padreId);

        if (duplicado) {
            throw new IllegalArgumentException("Ya existe una característica con ese nombre en el mismo nivel.");
        }

        Caracteristica nueva = new Caracteristica();
        nueva.setNombre(nombreLimpio);

        if (padreId != null) {
            Caracteristica padre = caracteristicaRepository.findById(padreId)
                    .orElseThrow(() -> new IllegalArgumentException("El padre seleccionado no existe."));
            nueva.setPadre(padre);
        }

        return caracteristicaRepository.save(nueva);
    }

    /**
     * Elimina una característica.
     * Si tiene hijos, también se eliminan en cascada (por orphanRemoval).
     */
    @Transactional
    public void eliminar(Long id) {
        caracteristicaRepository.deleteById(id);
    }
}

