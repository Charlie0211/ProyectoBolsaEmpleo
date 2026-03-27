package org.project1.bolsaempleo.repository;

import org.project1.bolsaempleo.entity.Puesto;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PuestoRepository extends JpaRepository<Puesto, Long> {

	@EntityGraph(attributePaths = {"caracteristicas", "caracteristicas.caracteristica"})
	List<Puesto> findTop5ByEsPublicoTrueAndActivoTrueOrderByIdDesc();

	@EntityGraph(attributePaths = {"caracteristicas", "caracteristicas.caracteristica"})
	Optional<Puesto> findByIdAndEsPublicoTrueAndActivoTrue(Long id);

	@EntityGraph(attributePaths = {"caracteristicas", "caracteristicas.caracteristica"})
	List<Puesto> findByEsPublicoTrueAndActivoTrueOrderByIdDesc();
}