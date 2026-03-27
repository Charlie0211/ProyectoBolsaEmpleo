package org.project1.bolsaempleo.controller;

import org.project1.bolsaempleo.service.CaracteristicaService;
import org.project1.bolsaempleo.service.PuestoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PuestoController {

	private final CaracteristicaService caracteristicaService;
	private final PuestoService puestoService;

	public PuestoController(CaracteristicaService caracteristicaService,
						   PuestoService puestoService) {
		this.caracteristicaService = caracteristicaService;
		this.puestoService = puestoService;
	}

	@GetMapping("/puestos")
	public String buscarPuestos(@RequestParam(value = "caracteristicaIds", required = false) List<Long> caracteristicaIds,
							@RequestParam Map<String, String> requestParams,
								Model model) {
		List<Long> seleccionadas = (caracteristicaIds == null) ? new ArrayList<>() : caracteristicaIds;
		Map<Long, Integer> selectedNiveles = new HashMap<>();

		for (Long id : seleccionadas) {
			String key = "nivelesPorCaracteristica[" + id + "]";
			int nivel = 3;
			if (requestParams.containsKey(key)) {
				try {
					int parsed = Integer.parseInt(requestParams.get(key));
					nivel = (parsed >= 1 && parsed <= 5) ? parsed : 3;
				} catch (NumberFormatException ignored) {
					nivel = 3;
				}
			}
			selectedNiveles.put(id, nivel);
		}

		model.addAttribute("raices", caracteristicaService.obtenerRaices());
		model.addAttribute("selectedCaracteristicaIds", seleccionadas);
		model.addAttribute("selectedNiveles", selectedNiveles);
		model.addAttribute("resultadosPuestos", puestoService.buscarConCoincidencia(selectedNiveles));
		return "puestos";
	}
}
