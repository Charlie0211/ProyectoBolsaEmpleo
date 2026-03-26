package org.project1.bolsaempleo.controller;

import jakarta.servlet.http.HttpSession;
import org.project1.bolsaempleo.service.AuthenticatedUser;
import org.project1.bolsaempleo.service.CaracteristicaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final CaracteristicaService caracteristicaService;

    public AdminController(CaracteristicaService caracteristicaService) {
        this.caracteristicaService = caracteristicaService;
    }

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

    /** GET /admin/caracteristicas — muestra catálogo completo + formulario */
    @GetMapping("/caracteristicas")
    public String caracteristicas(HttpSession session, Model model) {
        if (!verificarRolAdmin(session, model)) {
            return "redirect:/login";
        }

        // Todas las características raíz para la tabla
        model.addAttribute("raices", caracteristicaService.obtenerRaices());
        // Solo las raíces pueden ser padre — evita jerarquías de más de 2 niveles
        model.addAttribute("padresCaracteristica", caracteristicaService.obtenerRaices());
        return "admin-caracteristicas";
    }

    /** POST /admin/caracteristicas — crea una nueva característica */
    @PostMapping("/caracteristicas")
    public String crearCaracteristica(@RequestParam("nombre") String nombre,
                                      @RequestParam(value = "padreId", required = false) Long padreId,
                                      HttpSession session,
                                      RedirectAttributes redirectAttributes) {
        if (session.getAttribute("authenticatedUser") == null) {
            return "redirect:/login";
        }

        try {
            caracteristicaService.crear(nombre, padreId);
            redirectAttributes.addFlashAttribute("mensajeExito", "Característica creada correctamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        }
        return "redirect:/admin/caracteristicas";
    }

    /** POST /admin/caracteristicas/{id}/eliminar — elimina una característica */
    @PostMapping("/caracteristicas/{id}/eliminar")
    public String eliminarCaracteristica(@PathVariable Long id,
                                         HttpSession session,
                                         RedirectAttributes redirectAttributes) {
        if (session.getAttribute("authenticatedUser") == null) {
            return "redirect:/login";
        }

        try {
            caracteristicaService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Característica eliminada.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "No se pudo eliminar la característica.");
        }
        return "redirect:/admin/caracteristicas";
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
