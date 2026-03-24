package org.project1.bolsaempleo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project1.bolsaempleo.entity.Administrador;
import org.project1.bolsaempleo.entity.Empresa;
import org.project1.bolsaempleo.entity.Oferente;
import org.project1.bolsaempleo.repository.AdministradorRepository;
import org.project1.bolsaempleo.repository.EmpresaRepository;
import org.project1.bolsaempleo.repository.OferenteRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTests {

    @Mock
    private EmpresaRepository empresaRepository;

    @Mock
    private OferenteRepository oferenteRepository;

    @Mock
    private AdministradorRepository administradorRepository;

    private PasswordEncoder passwordEncoder;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        authService = new AuthService(empresaRepository, oferenteRepository, administradorRepository, passwordEncoder);
    }

    @Test
    void shouldAuthenticateApprovedEmpresa() {
        Empresa empresa = new Empresa();
        empresa.setId(1L);
        empresa.setNombre("Empresa Demo");
        empresa.setCorreo("empresa@demo.com");
        empresa.setClave(passwordEncoder.encode("empresa123"));
        empresa.setAprobada(Boolean.TRUE);

        when(empresaRepository.findByCorreoIgnoreCase("empresa@demo.com")).thenReturn(Optional.of(empresa));

        Optional<AuthenticatedUser> result = authService.authenticate("empresa@demo.com", "empresa123", "empresa");

        assertTrue(result.isPresent());
        assertEquals("empresa", result.get().rol());
        assertEquals("Empresa Demo", result.get().nombreVisible());
    }

    @Test
    void shouldRejectUnapprovedOferente() {
        Oferente oferente = new Oferente();
        oferente.setId(2L);
        oferente.setNombre("Oferente Demo");
        oferente.setCorreo("oferente@demo.com");
        oferente.setClave(passwordEncoder.encode("oferente123"));
        oferente.setAprobada(Boolean.FALSE);

        when(oferenteRepository.findByCorreoIgnoreCase("oferente@demo.com")).thenReturn(Optional.of(oferente));

        Optional<AuthenticatedUser> result = authService.authenticate("oferente@demo.com", "oferente123", "oferente");

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldAuthenticateAdministradorByIdentificacion() {
        Administrador administrador = new Administrador();
        administrador.setId(3L);
        administrador.setNombre("Administrador Demo");
        administrador.setIdentificacion("admin");
        administrador.setClave(passwordEncoder.encode("admin123"));

        when(administradorRepository.findByIdentificacionIgnoreCase("admin")).thenReturn(Optional.of(administrador));

        Optional<AuthenticatedUser> result = authService.authenticate("admin", "admin123", "admin");

        assertTrue(result.isPresent());
        assertEquals("admin", result.get().rol());
        assertEquals("Administrador Demo", result.get().nombreVisible());
    }

    @Test
    void shouldRejectInvalidPassword() {
        Empresa empresa = new Empresa();
        empresa.setId(1L);
        empresa.setNombre("Empresa Demo");
        empresa.setCorreo("empresa@demo.com");
        empresa.setClave(passwordEncoder.encode("empresa123"));
        empresa.setAprobada(Boolean.TRUE);

        when(empresaRepository.findByCorreoIgnoreCase("empresa@demo.com")).thenReturn(Optional.of(empresa));

        Optional<AuthenticatedUser> result = authService.authenticate("empresa@demo.com", "incorrecta", "empresa");

        assertTrue(result.isEmpty());
    }
}

