package org.project1.bolsaempleo.controller;

import jakarta.servlet.http.HttpSession;
import org.project1.bolsaempleo.entity.Puesto;
import org.project1.bolsaempleo.repository.PuestoRepository;
import org.project1.bolsaempleo.service.AuthenticatedUser;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EmpresaController {

    private final PuestoRepository puestoRepository;

    public EmpresaController(PuestoRepository puestoRepository) {
        this.puestoRepository = puestoRepository;
    }

    @GetMapping("/empresa/mis-puestos")
    public String misPuestos(HttpSession session, Model model) {
        String view = empresaView(session, model, "empresa-mis-puestos");
        if (view.startsWith("redirect:")) {
            return view;
        }

        model.addAttribute("puestos", puestoRepository.findAll(Sort.by(Sort.Direction.DESC, "id")));
        return view;
    }

    @GetMapping("/empresa/publicar-puesto")
    public String publicarPuesto(HttpSession session, Model model) {
        return empresaView(session, model, "empresa-publicar-puesto");
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
        if (view.startsWith("redirect:")) {
            return view;
        }

        Puesto puesto = puestoRepository.findById(puestoId).orElse(null);
        model.addAttribute("puesto", puesto);
        return view;
    }

    private String empresaView(HttpSession session, Model model, String viewName) {
        Object sessionUser = session.getAttribute("authenticatedUser");
        if (!(sessionUser instanceof AuthenticatedUser authenticatedUser)) {
            return "redirect:/login";
        }

        if (!"empresa".equals(authenticatedUser.rol())) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", authenticatedUser);
        model.addAttribute("mensajeBienvenida", "Bienvenido empresa");
        return viewName;
    }
}

