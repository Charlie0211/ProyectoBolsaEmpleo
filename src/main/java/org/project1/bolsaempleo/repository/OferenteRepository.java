package org.project1.bolsaempleo.repository;

import org.project1.bolsaempleo.entity.Oferente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface OferenteRepository extends JpaRepository<Oferente, Long> {
	Optional<Oferente> findByCorreoIgnoreCase(String correo);
}
