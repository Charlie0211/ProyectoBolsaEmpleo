package org.project1.bolsaempleo.controller;

import jakarta.servlet.http.HttpSession;
import org.project1.bolsaempleo.service.AuthService;
import org.project1.bolsaempleo.service.AuthenticatedUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/auth/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam("role") String role,
                        HttpSession session,
                        Model model) {
        Optional<AuthenticatedUser> authenticatedUser = authService.authenticate(username, password, role);

        if (authenticatedUser.isEmpty()) {
            model.addAttribute("error", "Credenciales inválidas o usuario pendiente de aprobación.");
            return "login";
        }

        AuthenticatedUser user = authenticatedUser.get();
        session.setAttribute("authenticatedUser", user);

        return switch (user.rol()) {
            case "empresa" -> "redirect:/empresa/dashboard";
            case "oferente" -> "redirect:/oferente/dashboard";
            case "admin" -> "redirect:/admin/dashboard";
            default -> "redirect:/login";
        };
    }

    @PostMapping("/auth/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}

