package org.project1.bolsaempleo.repository;

import org.project1.bolsaempleo.entity.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Long> {
    Optional<Administrador> findByIdentificacionIgnoreCase(String identificacion);
}

