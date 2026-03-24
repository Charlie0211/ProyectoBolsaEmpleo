package org.project1.bolsaempleo.repository;

import org.project1.bolsaempleo.entity.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
	Optional<Empresa> findByCorreoIgnoreCase(String correo);
}
