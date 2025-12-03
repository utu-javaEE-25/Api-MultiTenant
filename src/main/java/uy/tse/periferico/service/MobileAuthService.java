package uy.tse.periferico.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uy.tse.periferico.dto.MobileAuthTokensResponse;
import uy.tse.periferico.dto.ProfesionalDTO;
import uy.tse.periferico.model.Profesional;
import uy.tse.periferico.repository.ProfesionalRepository;
import uy.tse.periferico.security.JwtTokenProvider;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class MobileAuthService {

    private final ProfesionalRepository profesionalRepository;
    private final ProfesionalService profesionalService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * mobileAuthCode -> username (profesional)
     */
    private static final Map<String, String> CODE_STORE = new ConcurrentHashMap<>();

    /**
     * Construye la URL real de login que la app abrirá.
     * (Más adelante irá gub.uy aquí.)
     */
    public String buildMobileLoginUrl(String tenantId) {
        return "https://TU_BACKEND/" + tenantId + "/api/auth/login/profesional";
    }

    /**
     * Usado por AuthController cuando detecta login desde origen = mobile
     */
    public String generateMobileAuthCode(String username) {
        String code = UUID.randomUUID().toString();
        CODE_STORE.put(code, username);
        return code;
    }

    /**
     * Intercambio del mobileAuthCode -> token real + ProfesionalDTO
     */
    public MobileAuthTokensResponse exchangeMobileAuthCode(String code, String tenantId) {

        if (!CODE_STORE.containsKey(code)) {
            throw new RuntimeException("Código inválido o expirado.");
        }

        String username = CODE_STORE.get(code);

        Profesional profesional = profesionalRepository
                .findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Profesional no encontrado: " + username));

        ProfesionalDTO dto = profesionalService.toDTO(profesional);

        String token = jwtTokenProvider.generateToken(
                profesional.getUsername(),
                profesional.getId(),
                tenantId,
                "PROFESIONAL");

        CODE_STORE.remove(code);

        return new MobileAuthTokensResponse(token, dto);
    }
}
