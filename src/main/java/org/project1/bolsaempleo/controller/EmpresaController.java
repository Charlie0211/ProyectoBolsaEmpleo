package org.project1.bolsaempleo.controller;

import jakarta.servlet.http.HttpSession;
import org.project1.bolsaempleo.entity.Puesto;
import org.project1.bolsaempleo.repository.PuestoRepository;
import org.project1.bolsaempleo.service.AuthenticatedUser;
import org.project1.bolsaempleo.service.CaracteristicaService;
import org.project1.bolsaempleo.service.PuestoService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class EmpresaController {

    private final PuestoRepository puestoRepository;
    private final PuestoService puestoService;
    private final CaracteristicaService caracteristicaService;

    public EmpresaController(PuestoRepository puestoRepository,
                             PuestoService puestoService,
                             CaracteristicaService caracteristicaService) {
        this.puestoRepository = puestoRepository;
        this.puestoService = puestoService;
        this.caracteristicaService = caracteristicaService;
    }

    @GetMapping("/empresa/mis-puestos")
    public String misPuestos(HttpSession session, Model model) {
        String view = empresaView(session, model, "empresa-mis-puestos");
        if (view.startsWith("redirect:")) return view;

        model.addAttribute("puestos", puestoRepository.findAll(Sort.by(Sort.Direction.DESC, "id")));
        return view;
    }

    @GetMapping("/empresa/publicar-puesto")
    public String publicarPuesto(HttpSession session, Model model) {
        String view = empresaView(session, model, "empresa-publicar-puesto");
        if (view.startsWith("redirect:")) return view;

        if (!model.containsAttribute("puesto")) {
            model.addAttribute("puesto", new Puesto());
        }
        // Pasar categorías raíz con sus hijos para el dropdown de características
        model.addAttribute("raices", caracteristicaService.obtenerRaices());
        return view;
    }

    @PostMapping("/empresa/publicar-puesto")
    public String guardarPuesto(@ModelAttribute("puesto") Puesto puesto,
                                @RequestParam(value = "caracteristicaIds", required = false) List<Long> caracIds,
                                @RequestParam(value = "niveles", required = false) List<Integer> niveles,
                                HttpSession session,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        String view = empresaView(session, model, "empresa-publicar-puesto");
        if (view.startsWith("redirect:")) return view;

        List<String> errores = validarPuesto(puesto);
        if (!errores.isEmpty()) {
            model.addAttribute("errores", errores);
            model.addAttribute("raices", caracteristicaService.obtenerRaices());
            return view;
        }

        if (puesto.getActivo() == null) puesto.setActivo(Boolean.TRUE);
        if (puesto.getEsPublico() == null) puesto.setEsPublico(Boolean.TRUE);

        puestoService.guardarConCaracteristicas(puesto, caracIds, niveles);
        redirectAttributes.addFlashAttribute("mensajeExito", "Puesto publicado correctamente.");
        return "redirect:/empresa/mis-puestos";
    }

    @PostMapping("/empresa/puestos/{id}/desactivar")
    public String desactivarPuesto(@PathVariable Long id, HttpSession session) {
        Object sessionUser = session.getAttribute("authenticatedUser");
        if (!(sessionUser instanceof AuthenticatedUser authenticatedUser) || !"empresa".equals(authenticatedUser.rol())) {
            return "redirect:/login";
        }

        puestoRepository.findById(id).ifPresent(puesto -> {
            puesto.setActivo(false);
            puestoRepository.save(puesto);
        });

        return "redirect:/empresa/mis-puestos";
    }

    @GetMapping("/empresa/candidatos")
    public String buscarCandidatos(@RequestParam("puestoId") Long puestoId, HttpSession session, Model model) {
        String view = empresaView(session, model, "empresa-candidatos");
        if (view.startsWith("redirect:")) return view;

        Puesto puesto = puestoRepository.findById(puestoId).orElse(null);
        model.addAttribute("puesto", puesto);
        return view;
    }

    private String empresaView(HttpSession session, Model model, String viewName) {
        Object sessionUser = session.getAttribute("authenticatedUser");
        if (!(sessionUser instanceof AuthenticatedUser authenticatedUser)) return "redirect:/login";
        if (!"empresa".equals(authenticatedUser.rol())) return "redirect:/login";
        model.addAttribute("usuario", authenticatedUser);
        model.addAttribute("mensajeBienvenida", "Bienvenido empresa");
        return viewName;
    }

    private List<String> validarPuesto(Puesto puesto) {
        List<String> errores = new ArrayList<>();
        if (puesto.getTitulo() == null || puesto.getTitulo().trim().isEmpty())
            errores.add("El titulo es obligatorio.");
        if (puesto.getDescripcion() == null || puesto.getDescripcion().trim().isEmpty())
            errores.add("La descripcion es obligatoria.");
        if (puesto.getSalario() == null)
            errores.add("El salario es obligatorio.");
        else if (puesto.getSalario() < 0)
            errores.add("El salario no puede ser negativo.");
        return errores;
    }
}
