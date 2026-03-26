package org.project1.bolsaempleo.repository;

import org.project1.bolsaempleo.entity.PuestoCaracteristica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PuestoCaracteristicaRepository extends JpaRepository<PuestoCaracteristica, Long> {

    /** Devuelve todas las características de un puesto dado */
    List<PuestoCaracteristica> findByPuestoId(Long puestoId);

    /** Elimina todas las características de un puesto (útil al actualizar) */
    void deleteByPuestoId(Long puestoId);
}

