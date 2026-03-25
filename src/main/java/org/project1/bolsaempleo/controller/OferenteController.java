package org.project1.bolsaempleo.controller;

import jakarta.servlet.http.HttpSession;
import org.project1.bolsaempleo.service.AuthenticatedUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OferenteController {

    @GetMapping("/oferente/mis-habilidades")
    public String misHabilidades(HttpSession session, Model model) {
        return oferenteView(session, model, "oferente-mis-habilidades");
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

