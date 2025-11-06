package uy.tse.periferico.controller;

import lombok.RequiredArgsConstructor;
import uy.tse.periferico.dto.DocumentoClinicoDTO;
import uy.tse.periferico.service.DocumentoClinicoService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/{tenantId}/api/documentos")
@RequiredArgsConstructor
public class DocumentoClinicoController {

    private final DocumentoClinicoService documentoClinicoService;

    @GetMapping("/{idExternaDoc}")
    public ResponseEntity<DocumentoClinicoDTO> getDocumentoByIdExterna(@PathVariable String idExternaDoc) {
        
        DocumentoClinicoDTO documento = documentoClinicoService.findDocumentoByIdExterna(idExternaDoc);
        return ResponseEntity.ok(documento);
    }
}