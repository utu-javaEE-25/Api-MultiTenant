package uy.tse.periferico.controller;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uy.tse.periferico.dto.SolicitudAccesoRequestDTO;
import uy.tse.periferico.model.Profesional; 
import uy.tse.periferico.repository.ProfesionalRepository;
import uy.tse.periferico.security.JwtTokenProvider;
import uy.tse.periferico.service.HcenAccesoService;

@RestController
@RequestMapping("/{tenantId}/api/accesos")
@RequiredArgsConstructor
public class AccesoController {

    private final HcenAccesoService accesoService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ProfesionalRepository profesionalRepository;

    @PostMapping("/solicitar")
    public ResponseEntity<String> solicitarAcceso(
            @RequestHeader("Authorization") String authHeader,
            @AuthenticationPrincipal String username,
            @PathVariable String tenantId, 
            @RequestBody SolicitudAccesoRequestDTO dto) {
        
        String token = authHeader.substring(7);
        Claims claims = jwtTokenProvider.validateAndGetClaims(token);
        Long profesionalId = claims.get("profesional_id", Long.class);

        if (profesionalId == null) {
            return ResponseEntity.badRequest().body("El token del profesional no contiene un ID válido.");
        }

        Profesional profesional = profesionalRepository.findByUsername(username)
                .orElse(null);
        
        if (profesional == null) {
            return ResponseEntity.status(404).body("No se encontró el perfil del profesional solicitante.");
        }
        
        dto.setSchemaTenantSolicitante(tenantId);
        dto.setIdProfesionalSolicitante(profesionalId);
        dto.setNombreProfesionalSolicitante(profesional.getNombre() + " " + profesional.getApellido());
        
        try {
            String respuesta = accesoService.solicitarAcceso(dto);
            return ResponseEntity.ok(respuesta);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
