package org.project1.bolsaempleo.controller;

import org.project1.bolsaempleo.service.CaracteristicaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PuestoController {

	private final CaracteristicaService caracteristicaService;

	public PuestoController(CaracteristicaService caracteristicaService) {
		this.caracteristicaService = caracteristicaService;
	}

	@GetMapping("/puestos")
	public String buscarPuestos(@RequestParam(value = "caracteristicaIds", required = false) List<Long> caracteristicaIds,
								Model model) {
		List<Long> seleccionadas = (caracteristicaIds == null) ? new ArrayList<>() : caracteristicaIds;

		model.addAttribute("raices", caracteristicaService.obtenerRaices());
		model.addAttribute("selectedCaracteristicaIds", seleccionadas);
		return "puestos";
	}
}
