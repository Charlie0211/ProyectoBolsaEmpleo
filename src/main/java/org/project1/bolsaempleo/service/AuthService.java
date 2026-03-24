package org.project1.bolsaempleo.service;

import org.project1.bolsaempleo.entity.Administrador;
import org.project1.bolsaempleo.entity.Empresa;
import org.project1.bolsaempleo.entity.Oferente;
import org.project1.bolsaempleo.repository.AdministradorRepository;
import org.project1.bolsaempleo.repository.EmpresaRepository;
import org.project1.bolsaempleo.repository.OferenteRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

@Service
public class AuthService {

    private final EmpresaRepository empresaRepository;
    private final OferenteRepository oferenteRepository;
    private final AdministradorRepository administradorRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(EmpresaRepository empresaRepository,
                       OferenteRepository oferenteRepository,
                       AdministradorRepository administradorRepository,
                       PasswordEncoder passwordEncoder) {
        this.empresaRepository = empresaRepository;
        this.oferenteRepository = oferenteRepository;
        this.administradorRepository = administradorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<AuthenticatedUser> authenticate(String username, String password, String role) {
        if (isBlank(username) || isBlank(password) || isBlank(role)) {
            return Optional.empty();
        }

        return switch (role.trim().toLowerCase(Locale.ROOT)) {
            case "empresa" -> authenticateEmpresa(username.trim(), password);
            case "oferente" -> authenticateOferente(username.trim(), password);
            case "admin" -> authenticateAdministrador(username.trim(), password);
            default -> Optional.empty();
        };
    }

    private Optional<AuthenticatedUser> authenticateEmpresa(String correo, String password) {
        return empresaRepository.findByCorreoIgnoreCase(correo)
                .filter(empresa -> Boolean.TRUE.equals(empresa.getAprobada()))
                .filter(empresa -> passwordMatches(password, empresa.getClave()))
                .map(empresa -> new AuthenticatedUser(
                        empresa.getId(),
                        "empresa",
                        displayName(empresa.getNombre(), empresa.getCorreo())
                ));
    }

    private Optional<AuthenticatedUser> authenticateOferente(String correo, String password) {
        return oferenteRepository.findByCorreoIgnoreCase(correo)
                .filter(oferente -> Boolean.TRUE.equals(oferente.getAprobada()))
                .filter(oferente -> passwordMatches(password, oferente.getClave()))
                .map(oferente -> new AuthenticatedUser(
                        oferente.getId(),
                        "oferente",
                        displayName(oferente.getNombre(), oferente.getCorreo())
                ));
    }

    private Optional<AuthenticatedUser> authenticateAdministrador(String identificacion, String password) {
        return administradorRepository.findByIdentificacionIgnoreCase(identificacion)
                .filter(admin -> passwordMatches(password, admin.getClave()))
                .map(admin -> new AuthenticatedUser(
                        admin.getId(),
                        "admin",
                        displayName(admin.getNombre(), admin.getIdentificacion())
                ));
    }

    private boolean passwordMatches(String rawPassword, String encodedPassword) {
        return encodedPassword != null && passwordEncoder.matches(rawPassword, encodedPassword);
    }

    private String displayName(String preferred, String fallback) {
        if (!isBlank(preferred)) {
            return preferred;
        }
        return fallback;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}

