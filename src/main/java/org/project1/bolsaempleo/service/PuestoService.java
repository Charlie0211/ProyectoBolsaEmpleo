package org.project1.bolsaempleo.service;

import org.project1.bolsaempleo.entity.Puesto;
import org.project1.bolsaempleo.entity.PuestoCaracteristica;
import org.project1.bolsaempleo.repository.CaracteristicaRepository;
import org.project1.bolsaempleo.repository.PuestoCaracteristicaRepository;
import org.project1.bolsaempleo.repository.PuestoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PuestoService {

    private final PuestoRepository puestoRepository;
    private final CaracteristicaRepository caracteristicaRepository;
    private final PuestoCaracteristicaRepository puestoCaracteristicaRepository;

    public PuestoService(PuestoRepository puestoRepository,
                         CaracteristicaRepository caracteristicaRepository,
                         PuestoCaracteristicaRepository puestoCaracteristicaRepository) {
        this.puestoRepository = puestoRepository;
        this.caracteristicaRepository = caracteristicaRepository;
        this.puestoCaracteristicaRepository = puestoCaracteristicaRepository;
    }

    /**
     * Guarda un puesto y sus características requeridas en una sola transacción.
     * @param puesto    entidad del puesto a guardar
     * @param caracIds  lista de IDs de características seleccionadas (alineada con niveles)
     * @param niveles   lista de niveles requeridos 1-5 (alineada con caracIds)
     */
    @Transactional
    public Puesto guardarConCaracteristicas(Puesto puesto, List<Long> caracIds, List<Integer> niveles) {
        Puesto savedPuesto = puestoRepository.save(puesto);

        if (caracIds != null) {
            for (int i = 0; i < caracIds.size(); i++) {
                final Long caracId = caracIds.get(i);
                if (caracId == null || caracId == 0L) continue;

                final Integer nivel = (niveles != null && i < niveles.size() && niveles.get(i) != null)
                        ? niveles.get(i) : 1;

                caracteristicaRepository.findById(caracId).ifPresent(c -> {
                    PuestoCaracteristica pc = new PuestoCaracteristica(savedPuesto, c, nivel);
                    puestoCaracteristicaRepository.save(pc);
                });
            }
        }

        return savedPuesto;
    }
}

