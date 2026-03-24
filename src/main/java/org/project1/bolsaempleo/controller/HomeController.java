package org.project1.bolsaempleo.controller;

import jakarta.servlet.http.HttpSession;
import org.project1.bolsaempleo.repository.PuestoRepository;
import org.project1.bolsaempleo.service.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private PuestoRepository puestoRepository;


    @GetMapping("/")
    public String mostrarInicio(Model model) {
        model.addAttribute("listaPuestos", puestoRepository.findAll());
        return "index";
    }


    @GetMapping("/login")
    public String mostrarLogin(HttpSession session) {
        Object sessionUser = session.getAttribute("authenticatedUser");
        if (sessionUser instanceof AuthenticatedUser authenticatedUser) {
            return switch (authenticatedUser.rol()) {
                case "empresa" -> "redirect:/empresa/dashboard";
                case "oferente" -> "redirect:/oferente/dashboard";
                case "admin" -> "redirect:/admin/dashboard";
                default -> "login";
            };
        }

        return "login";
    }
}
