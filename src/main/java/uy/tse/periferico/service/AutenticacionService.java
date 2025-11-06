package uy.tse.periferico.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uy.tse.periferico.dto.LoginRequest;
import uy.tse.periferico.model.AdminTenant;
import uy.tse.periferico.model.Profesional;
import uy.tse.periferico.repository.AdminTenantRepository;
import uy.tse.periferico.repository.ProfesionalRepository;
import uy.tse.periferico.security.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class AutenticacionService {

    private final ProfesionalRepository profesionalRepository;
    private final AdminTenantRepository adminTenantRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public String loginProfesional(LoginRequest loginRequest, String tenantId) {
        String username = loginRequest.getUsername();


        Profesional profesional = profesionalRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario profesional no encontrado: " + username));

        if (!passwordEncoder.matches(loginRequest.getPassword(), profesional.getPasswordHash())) {
            throw new BadCredentialsException("Contraseña incorrecta para el profesional.");
        }

        return jwtTokenProvider.generateToken(profesional.getUsername(), tenantId, "PROFESIONAL");
    }

    public String loginAdmin(LoginRequest loginRequest, String tenantId) {
        String username = loginRequest.getUsername();

        AdminTenant admin = adminTenantRepository.findByNombreUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario administrador no encontrado: " + username));

        if (!passwordEncoder.matches(loginRequest.getPassword(), admin.getPasswordHash())) {
            throw new BadCredentialsException("Contraseña incorrecta para el administrador.");
        }

        return jwtTokenProvider.generateToken(admin.getNombreUsuario(), tenantId, "ADMIN");
    }
}