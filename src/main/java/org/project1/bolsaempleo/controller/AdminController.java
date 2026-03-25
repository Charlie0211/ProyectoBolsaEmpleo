package org.project1.bolsaempleo.controller;

import jakarta.servlet.http.HttpSession;
import org.project1.bolsaempleo.service.AuthenticatedUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    /**
     * Muestra la lista de empresas pendientes de aprobación
     */
    @GetMapping("/empresas-pendientes")
    public String empresasPendientes(HttpSession session, Model model) {
        if (!verificarRolAdmin(session, model)) {
            return "redirect:/login";
        }
        // Aquí se agregaría la lógica para obtener empresas pendientes de la base de datos
        return "admin-empresas-pendientes";
    }

    /**
     * Muestra la lista de oferentes pendientes de aprobación
     */
    @GetMapping("/oferentes-pendientes")
    public String oferentesPendientes(HttpSession session, Model model) {
        if (!verificarRolAdmin(session, model)) {
            return "redirect:/login";
        }
        // Aquí se agregaría la lógica para obtener oferentes pendientes de la base de datos
        return "admin-oferentes-pendientes";
    }

    /**
     * Muestra el catálogo de características (puede crear, editar, eliminar)
     */
    @GetMapping("/caracteristicas")
    public String caracteristicas(HttpSession session, Model model) {
        if (!verificarRolAdmin(session, model)) {
            return "redirect:/login";
        }
        // Aquí se agregaría la lógica para obtener todas las características de la base de datos
        return "admin-caracteristicas";
    }

    /**
     * Muestra la página de reportes PDF
     */
    @GetMapping("/reportes")
    public String reportes(HttpSession session, Model model) {
        if (!verificarRolAdmin(session, model)) {
            return "redirect:/login";
        }
        // Aquí se agregaría la lógica para generar reportes
        return "admin-reportes";
    }

    /**
     * Verifica que el usuario tenga el rol de administrador
     * @param session sesión HTTP actual
     * @param model modelo para pasar atributos a la vista
     * @return true si es admin, false en caso contrario
     */
    private boolean verificarRolAdmin(HttpSession session, Model model) {
        Object sessionUser = session.getAttribute("authenticatedUser");
        if (!(sessionUser instanceof AuthenticatedUser authenticatedUser)) {
            return false;
        }

        if (!"admin".equals(authenticatedUser.rol())) {
            return false;
        }

        model.addAttribute("usuario", authenticatedUser);
        return true;
    }
}

