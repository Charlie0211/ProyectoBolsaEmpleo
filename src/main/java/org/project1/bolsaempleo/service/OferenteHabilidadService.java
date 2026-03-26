package org.project1.bolsaempleo.service;

import org.project1.bolsaempleo.entity.Caracteristica;
import org.project1.bolsaempleo.entity.Oferente;
import org.project1.bolsaempleo.entity.OferenteHabilidad;
import org.project1.bolsaempleo.repository.CaracteristicaRepository;
import org.project1.bolsaempleo.repository.OferenteHabilidadRepository;
import org.project1.bolsaempleo.repository.OferenteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OferenteHabilidadService {

    private final OferenteHabilidadRepository oferenteHabilidadRepository;
    private final OferenteRepository oferenteRepository;
    private final CaracteristicaRepository caracteristicaRepository;

    public OferenteHabilidadService(OferenteHabilidadRepository oferenteHabilidadRepository,
                                    OferenteRepository oferenteRepository,
                                    CaracteristicaRepository caracteristicaRepository) {
        this.oferenteHabilidadRepository = oferenteHabilidadRepository;
        this.oferenteRepository = oferenteRepository;
        this.caracteristicaRepository = caracteristicaRepository;
    }

    public List<OferenteHabilidad> listarPorOferente(Long oferenteId) {
        return oferenteHabilidadRepository.findByOferenteIdOrderByIdDesc(oferenteId);
    }

    @Transactional
    public void guardarOActualizar(Long oferenteId, Long caracteristicaId, Integer nivel) {
        if (nivel == null || nivel < 1 || nivel > 5) {
            throw new IllegalArgumentException("El nivel debe estar entre 1 y 5.");
        }

        Oferente oferente = oferenteRepository.findById(oferenteId)
                .orElseThrow(() -> new IllegalArgumentException("El oferente no existe."));

        Caracteristica caracteristica = caracteristicaRepository.findById(caracteristicaId)
                .orElseThrow(() -> new IllegalArgumentException("La característica no existe."));

        OferenteHabilidad habilidad = oferenteHabilidadRepository
                .findByOferenteIdAndCaracteristicaId(oferenteId, caracteristicaId)
                .orElse(new OferenteHabilidad(oferente, caracteristica, nivel));

        habilidad.setNivel(nivel);
        oferenteHabilidadRepository.save(habilidad);
    }

    @Transactional
    public void eliminarDeOferente(Long oferenteId, Long habilidadId) {
        OferenteHabilidad habilidad = oferenteHabilidadRepository
                .findByIdAndOferenteId(habilidadId, oferenteId)
                .orElseThrow(() -> new IllegalArgumentException("La habilidad no existe o no pertenece al oferente."));

        oferenteHabilidadRepository.delete(habilidad);
    }
}

