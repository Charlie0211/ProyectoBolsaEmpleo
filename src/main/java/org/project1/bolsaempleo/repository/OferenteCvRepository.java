package org.project1.bolsaempleo.repository;

import org.project1.bolsaempleo.entity.OferenteCv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OferenteCvRepository extends JpaRepository<OferenteCv, Long> {

    Optional<OferenteCv> findByOferenteId(Long oferenteId);
}

