package uy.tse.periferico.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal; 
import org.springframework.web.bind.annotation.*; 
import uy.tse.periferico.dto.DocumentoClinicoCreateDTO; 
import uy.tse.periferico.dto.DocumentoClinicoDTO;
import uy.tse.periferico.service.DocumentoClinicoService;

@RestController
@RequestMapping("/{tenantId}/api/documentos")
@RequiredArgsConstructor
public class DocumentoClinicoController {

    private final DocumentoClinicoService documentoClinicoService;

     @PostMapping
    public ResponseEntity<DocumentoClinicoDTO> createDocumento(
            @PathVariable String tenantId,
            @RequestBody DocumentoClinicoCreateDTO createDTO,
            @AuthenticationPrincipal String username) {
        DocumentoClinicoDTO nuevoDocumento = documentoClinicoService.createDocumento(createDTO, username, tenantId);
        return new ResponseEntity<>(nuevoDocumento, HttpStatus.CREATED);
    }

    @GetMapping("/{idExternaDoc}")
    public ResponseEntity<DocumentoClinicoDTO> getDocumentoByIdExterna(@PathVariable String idExternaDoc) {
        
        DocumentoClinicoDTO documento = documentoClinicoService.findDocumentoByIdExterna(idExternaDoc);
        return ResponseEntity.ok(documento);
    }
}