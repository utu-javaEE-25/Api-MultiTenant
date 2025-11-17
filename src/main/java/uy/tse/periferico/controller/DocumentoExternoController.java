package uy.tse.periferico.controller;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uy.tse.periferico.dto.DocumentoDetalleDTO;
import uy.tse.periferico.security.JwtTokenProvider;
import uy.tse.periferico.service.HcenDocumentoExternoService;
import uy.tse.periferico.dto.DocumentoClinicoDTO;

@RestController
@RequestMapping("/{tenantId}/api/documento-externo")
@RequiredArgsConstructor
public class DocumentoExternoController {

    private final HcenDocumentoExternoService documentoExternoService;
    private final JwtTokenProvider jwtTokenProvider; 

    @GetMapping
    public ResponseEntity<?> getDocumento(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String tenantId,
            @RequestParam String cedulaPaciente,
            @RequestParam String docId) {

        String token = authHeader.substring(7);
        Claims claims = jwtTokenProvider.validateAndGetClaims(token);
        Long profesionalId = claims.get("profesional_id", Long.class);

        if (profesionalId == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("El token no corresponde a un profesional válido o no contiene ID.");
        }
        
        DocumentoClinicoDTO documento = documentoExternoService.obtenerDocumento(tenantId, cedulaPaciente, docId, profesionalId);
        return ResponseEntity.ok(documento);
    }
}