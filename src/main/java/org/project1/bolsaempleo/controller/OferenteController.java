package org.project1.bolsaempleo.controller;

import jakarta.servlet.http.HttpSession;
import org.project1.bolsaempleo.repository.OferenteRepository;
import org.project1.bolsaempleo.service.AuthenticatedUser;
import org.project1.bolsaempleo.service.CaracteristicaService;
import org.project1.bolsaempleo.service.OferenteHabilidadService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class OferenteController {

    private final CaracteristicaService caracteristicaService;
    private final OferenteHabilidadService oferenteHabilidadService;
    private final OferenteRepository oferenteRepository;

    public OferenteController(CaracteristicaService caracteristicaService,
                              OferenteHabilidadService oferenteHabilidadService,
                              OferenteRepository oferenteRepository) {
        this.caracteristicaService = caracteristicaService;
        this.oferenteHabilidadService = oferenteHabilidadService;
        this.oferenteRepository = oferenteRepository;
    }

    @GetMapping("/oferente/mis-habilidades")
    public String misHabilidades(HttpSession session, Model model) {
        String view = oferenteView(session, model, "oferente-mis-habilidades");
        if (view.startsWith("redirect:")) {
            return view;
        }

        Object sessionUser = session.getAttribute("authenticatedUser");
        if (sessionUser instanceof AuthenticatedUser authenticatedUser) {
            model.addAttribute("raices", caracteristicaService.obtenerRaices());
            model.addAttribute("habilidades", oferenteHabilidadService.listarPorOferente(authenticatedUser.id()));
            oferenteRepository.findById(authenticatedUser.id())
                    .ifPresent(oferente -> model.addAttribute("detalleOferente", oferente));
        }

        return view;
    }

    @PostMapping("/oferente/mis-habilidades")
    public String guardarHabilidad(@RequestParam("caracteristicaId") Long caracteristicaId,
                                   @RequestParam("nivel") Integer nivel,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        Object sessionUser = session.getAttribute("authenticatedUser");
        if (!(sessionUser instanceof AuthenticatedUser authenticatedUser) || !"oferente".equals(authenticatedUser.rol())) {
            return "redirect:/login";
        }

        try {
            if (caracteristicaId == null || caracteristicaId <= 0) {
                throw new IllegalArgumentException("Debes seleccionar una caracteristica valida.");
            }

            oferenteHabilidadService.guardarOActualizar(authenticatedUser.id(), caracteristicaId, nivel);
            redirectAttributes.addFlashAttribute("mensajeExito", "Habilidad guardada correctamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        }

        return "redirect:/oferente/mis-habilidades";
    }

    @PostMapping("/oferente/mis-habilidades/{id}/eliminar")
    public String eliminarHabilidad(@PathVariable("id") Long habilidadId,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        Object sessionUser = session.getAttribute("authenticatedUser");
        if (!(sessionUser instanceof AuthenticatedUser authenticatedUser) || !"oferente".equals(authenticatedUser.rol())) {
            return "redirect:/login";
        }

        try {
            oferenteHabilidadService.eliminarDeOferente(authenticatedUser.id(), habilidadId);
            redirectAttributes.addFlashAttribute("mensajeExito", "Habilidad eliminada correctamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        }

        return "redirect:/oferente/mis-habilidades";
    }

    @GetMapping("/oferente/mi-cv")
    public String miCv(HttpSession session, Model model) {
        return oferenteView(session, model, "oferente-mi-cv");
    }

    private String oferenteView(HttpSession session, Model model, String viewName) {
        Object sessionUser = session.getAttribute("authenticatedUser");
        if (!(sessionUser instanceof AuthenticatedUser authenticatedUser)) {
            return "redirect:/login";
        }

        if (!"oferente".equals(authenticatedUser.rol())) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", authenticatedUser);
        model.addAttribute("mensajeBienvenida", "Bienvenido oferente");
        return viewName;
    }
}

