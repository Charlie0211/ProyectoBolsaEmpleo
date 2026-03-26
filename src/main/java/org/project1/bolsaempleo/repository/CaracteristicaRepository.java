package org.project1.bolsaempleo.repository;

import org.project1.bolsaempleo.entity.Caracteristica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaracteristicaRepository extends JpaRepository<Caracteristica, Long> {

    /** Devuelve solo las características raíz (sin padre) */
    List<Caracteristica> findByPadreIsNullOrderByNombreAsc();

    /** Devuelve todas las características con un padre dado */
    List<Caracteristica> findByPadreIdOrderByNombreAsc(Long padreId);

    /** Devuelve todas ordenadas por nombre (útil para el dropdown) */
    List<Caracteristica> findAllByOrderByNombreAsc();

    /** Verifica si ya existe una característica con ese nombre bajo el mismo padre */
    boolean existsByNombreIgnoreCaseAndPadreId(String nombre, Long padreId);

    /** Verifica si ya existe una característica raíz con ese nombre */
    boolean existsByNombreIgnoreCaseAndPadreIsNull(String nombre);
}

