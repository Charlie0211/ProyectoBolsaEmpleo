package org.project1.bolsaempleo.controller;

import jakarta.servlet.http.HttpSession;
import org.project1.bolsaempleo.repository.OferenteRepository;
import org.project1.bolsaempleo.service.AuthenticatedUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final OferenteRepository oferenteRepository;

    public DashboardController(OferenteRepository oferenteRepository) {
        this.oferenteRepository = oferenteRepository;
    }

    @GetMapping("/empresa/dashboard")
    public String empresaDashboard(HttpSession session, Model model) {
        return dashboardForRole(session, model, "empresa", "empresa-dashboard");
    }

    @GetMapping("/oferente/dashboard")
    public String oferenteDashboard(HttpSession session, Model model) {
        String view = dashboardForRole(session, model, "oferente", "oferente-dashboard");
        if (view.startsWith("redirect:")) {
            return view;
        }

        Object sessionUser = session.getAttribute("authenticatedUser");
        if (sessionUser instanceof AuthenticatedUser authenticatedUser) {
            oferenteRepository.findById(authenticatedUser.id())
                    .ifPresent(oferente -> model.addAttribute("detalleOferente", oferente));
        }

        return view;
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(HttpSession session, Model model) {
        return dashboardForRole(session, model, "admin", "admin-dashboard");
    }

    private String dashboardForRole(HttpSession session, Model model, String expectedRole, String viewName) {
        Object sessionUser = session.getAttribute("authenticatedUser");
        if (!(sessionUser instanceof AuthenticatedUser authenticatedUser)) {
            return "redirect:/login";
        }

        if (!expectedRole.equals(authenticatedUser.rol())) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", authenticatedUser);
        model.addAttribute("mensajeBienvenida", mensajePorRol(expectedRole));
        return viewName;
    }

    private String mensajePorRol(String rol) {
        return switch (rol) {
            case "empresa" -> "Bienvenido empresa";
            case "oferente" -> "Bienvenido oferente";
            case "admin" -> "Bienvenido administrador";
            default -> "Bienvenido";
        };
    }
}

