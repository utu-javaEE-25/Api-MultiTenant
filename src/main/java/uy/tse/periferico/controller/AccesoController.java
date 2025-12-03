package uy.tse.periferico.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uy.tse.periferico.dto.SolicitudAccesoRequestDTO;
import uy.tse.periferico.service.HcenAccesoService;

@RestController
@RequestMapping("/{tenantId}/api/hcen")
@RequiredArgsConstructor
public class AccesoController {

    private final HcenAccesoService hcenAccesoService;

    /**
     * Endpoint para que un profesional solicite acceso a la historia clínica de un
     * paciente.
     * Este endpoint es consumido desde la app móvil.
     */
    @PostMapping("/solicitar-acceso")
    public ResponseEntity<String> solicitarAcceso(
            @PathVariable String tenantId,
            @RequestBody SolicitudAccesoRequestDTO solicitud,
            @AuthenticationPrincipal String username) {
        // Realiza la solicitud al HCEN y dispara notificación móvil
        String respuesta = hcenAccesoService.solicitarAcceso(solicitud, username);

        return ResponseEntity.ok(respuesta);
    }
}
