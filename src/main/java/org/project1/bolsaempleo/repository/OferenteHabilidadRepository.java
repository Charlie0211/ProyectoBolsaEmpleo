package org.project1.bolsaempleo.repository;

import org.project1.bolsaempleo.entity.OferenteHabilidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OferenteHabilidadRepository extends JpaRepository<OferenteHabilidad, Long> {

    List<OferenteHabilidad> findByOferenteIdOrderByIdDesc(Long oferenteId);

    Optional<OferenteHabilidad> findByOferenteIdAndCaracteristicaId(Long oferenteId, Long caracteristicaId);

    Optional<OferenteHabilidad> findByIdAndOferenteId(Long id, Long oferenteId);
}

