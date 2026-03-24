package org.project1.bolsaempleo.service;

import org.project1.bolsaempleo.entity.Administrador;
import org.project1.bolsaempleo.entity.Empresa;
import org.project1.bolsaempleo.entity.Oferente;
import org.project1.bolsaempleo.repository.AdministradorRepository;
import org.project1.bolsaempleo.repository.EmpresaRepository;
import org.project1.bolsaempleo.repository.OferenteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationDataInitializer {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner seedLoginUsers(EmpresaRepository empresaRepository,
                                            OferenteRepository oferenteRepository,
                                            AdministradorRepository administradorRepository,
                                            PasswordEncoder passwordEncoder) {
        return args -> {
            if (empresaRepository.findByCorreoIgnoreCase("empresa@demo.com").isEmpty()) {
                Empresa empresa = new Empresa();
                empresa.setNombre("Empresa Demo");
                empresa.setCorreo("empresa@demo.com");
                empresa.setClave(passwordEncoder.encode("empresa123"));
                empresa.setAprobada(Boolean.TRUE);
                empresaRepository.save(empresa);
            }

            if (oferenteRepository.findByCorreoIgnoreCase("oferente@demo.com").isEmpty()) {
                Oferente oferente = new Oferente();
                oferente.setNombre("Oferente Demo");
                oferente.setCorreo("oferente@demo.com");
                oferente.setClave(passwordEncoder.encode("oferente123"));
                oferente.setAprobada(Boolean.TRUE);
                oferenteRepository.save(oferente);
            }

            if (administradorRepository.findByIdentificacionIgnoreCase("admin").isEmpty()) {
                Administrador administrador = new Administrador();
                administrador.setNombre("Administrador Demo");
                administrador.setIdentificacion("admin");
                administrador.setClave(passwordEncoder.encode("admin123"));
                administradorRepository.save(administrador);
            }
        };
    }
}

