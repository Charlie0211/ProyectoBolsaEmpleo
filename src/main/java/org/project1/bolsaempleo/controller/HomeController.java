package org.project1.bolsaempleo.controller;

import org.project1.bolsaempleo.repository.PuestoRepository;
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
    public String mostrarLogin() {
        return "login";
    }
}
