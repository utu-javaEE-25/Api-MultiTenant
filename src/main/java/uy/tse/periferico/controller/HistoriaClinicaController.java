package uy.tse.periferico.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uy.tse.periferico.dto.DocumentoMetadataHcenDTO;
import uy.tse.periferico.service.HistoriaClinicaHcenService;

import java.util.List;

@RestController
@RequestMapping("/{tenantId}/api/historia-clinica")
@RequiredArgsConstructor
public class HistoriaClinicaController {

    private final HistoriaClinicaHcenService historiaService;

    @GetMapping("/{cedula}")
    public ResponseEntity<List<DocumentoMetadataHcenDTO>> getHistoria(
            @PathVariable String cedula,
            @AuthenticationPrincipal String username) {
        List<DocumentoMetadataHcenDTO> historia = historiaService.obtenerHistoriaPorCedula(cedula, username);

        return ResponseEntity.ok(historia);
    }
}
