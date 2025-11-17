package uy.tse.periferico.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uy.tse.periferico.dto.DocumentoMetadataHcenDTO;
import uy.tse.periferico.service.HistoriaClinicaHcenService;

import java.util.List;

@RestController
@RequestMapping("/{tenantId}/api/historia-clinica")
@RequiredArgsConstructor
public class HistoriaClinicaController {

    private final HistoriaClinicaHcenService historiaService;

    @GetMapping("/{cedula}")
    public ResponseEntity<List<DocumentoMetadataHcenDTO>> getHistoria(@PathVariable String cedula) {
        List<DocumentoMetadataHcenDTO> historia = historiaService.obtenerHistoriaPorCedula(cedula);
        return ResponseEntity.ok(historia);
    }
}
