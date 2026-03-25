package org.project1.bolsaempleo.controller;

import jakarta.servlet.http.HttpSession;
import org.project1.bolsaempleo.service.AuthenticatedUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EmpresaController {

    @GetMapping("/empresa/mis-puestos")
    public String misPuestos(HttpSession session, Model model) {
        return empresaView(session, model, "empresa-mis-puestos");
    }

    @GetMapping("/empresa/publicar-puesto")
    public String publicarPuesto(HttpSession session, Model model) {
        return empresaView(session, model, "empresa-publicar-puesto");
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

