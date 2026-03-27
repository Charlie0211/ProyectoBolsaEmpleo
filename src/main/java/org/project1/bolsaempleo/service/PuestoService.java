package org.project1.bolsaempleo.service;

import org.project1.bolsaempleo.entity.Puesto;
import org.project1.bolsaempleo.entity.PuestoCaracteristica;
import org.project1.bolsaempleo.repository.CaracteristicaRepository;
import org.project1.bolsaempleo.repository.PuestoCaracteristicaRepository;
import org.project1.bolsaempleo.repository.PuestoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * Busca puestos por coincidencia de características + nivel por característica.
     * Regla: si el puesto requiere nivel N, coincide cuando nivelUsuario >= N.
     *
     * @param nivelesPorCaracteristica mapa (caracteristicaId -> nivel usuario 1..5)
     * @return lista de resultados con porcentaje de coincidencia
     */
    public List<ResultadoBusquedaPuesto> buscarConCoincidencia(Map<Long, Integer> nivelesPorCaracteristica) {
        List<Puesto> puestos = puestoRepository.findByEsPublicoTrueAndActivoTrueOrderByIdDesc();
        List<ResultadoBusquedaPuesto> resultados = new ArrayList<>();

        int totalSeleccionadas = nivelesPorCaracteristica.size();

        for (Puesto puesto : puestos) {
            int coincidencias = contarCoincidencias(puesto, nivelesPorCaracteristica);

            // Si no hay filtros, se muestran todos con 0%.
            if (totalSeleccionadas == 0) {
                resultados.add(new ResultadoBusquedaPuesto(puesto, 0, 0, 0));
                continue;
            }

            // Mostrar puesto aunque solo tenga una coincidencia.
            if (coincidencias > 0) {
                int porcentaje = (int) Math.round((coincidencias * 100.0) / totalSeleccionadas);
                resultados.add(new ResultadoBusquedaPuesto(puesto, coincidencias, totalSeleccionadas, porcentaje));
            }
        }

        resultados.sort(Comparator
                .comparingInt(ResultadoBusquedaPuesto::getPorcentaje).reversed()
                .thenComparing(r -> r.getPuesto().getId(), Comparator.reverseOrder()));

        return resultados;
    }

    private int contarCoincidencias(Puesto puesto, Map<Long, Integer> nivelesPorCaracteristica) {
        Map<Long, Integer> nivelesRequeridosPuesto = new HashMap<>();
        for (PuestoCaracteristica pc : puesto.getCaracteristicas()) {
            nivelesRequeridosPuesto.put(pc.getCaracteristica().getId(), pc.getNivelRequerido());
        }

        int coincidencias = 0;
        for (Map.Entry<Long, Integer> criterio : nivelesPorCaracteristica.entrySet()) {
            Long caracteristicaId = criterio.getKey();
            Integer nivelUsuario = criterio.getValue();
            Integer nivelRequerido = nivelesRequeridosPuesto.get(caracteristicaId);

            if (nivelRequerido != null && nivelUsuario != null && nivelUsuario >= nivelRequerido) {
                coincidencias++;
            }
        }
        return coincidencias;
    }

    public static class ResultadoBusquedaPuesto {
        private final Puesto puesto;
        private final int coincidencias;
        private final int totalSeleccionadas;
        private final int porcentaje;

        public ResultadoBusquedaPuesto(Puesto puesto, int coincidencias, int totalSeleccionadas, int porcentaje) {
            this.puesto = puesto;
            this.coincidencias = coincidencias;
            this.totalSeleccionadas = totalSeleccionadas;
            this.porcentaje = porcentaje;
        }

        public Puesto getPuesto() {
            return puesto;
        }

        public int getCoincidencias() {
            return coincidencias;
        }

        public int getTotalSeleccionadas() {
            return totalSeleccionadas;
        }

        public int getPorcentaje() {
            return porcentaje;
        }

        public String getMensajeCoincidencia() {
            if (totalSeleccionadas == 0) {
                return "Sin filtros aplicados";
            }
            return porcentaje + "% de coincidencia (" + coincidencias + "/" + totalSeleccionadas + ")";
        }
    }
}

