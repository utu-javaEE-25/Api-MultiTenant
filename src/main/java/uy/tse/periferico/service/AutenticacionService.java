package uy.tse.periferico.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uy.tse.periferico.config.TenantContext;
import uy.tse.periferico.dto.LoginRequest;
import uy.tse.periferico.model.Profesional;
import uy.tse.periferico.repository.ProfesionalRepository;
import uy.tse.periferico.security.JwtTokenProvider;

@Service @RequiredArgsConstructor
public class AutenticacionService {
    private final ProfesionalRepository profesionalRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public String login(LoginRequest loginRequest, String tenantId) {

        Profesional profesional = profesionalRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario o tenant incorrecto"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), profesional.getPasswordHash())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return jwtTokenProvider.generateToken(profesional.getUsername(), tenantId);
    }
}
