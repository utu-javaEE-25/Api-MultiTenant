// RUTA: src/main/java/uy/tse/periferico/controller/ProfesionalController.java

package uy.tse.periferico.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uy.tse.periferico.dto.ProfesionalCreateDTO;
import uy.tse.periferico.dto.ProfesionalDTO;
import uy.tse.periferico.dto.ProfesionalUpdateDTO;
import uy.tse.periferico.service.ProfesionalService;

import java.util.List;

// --- CAMBIO ---: Se añade {tenantId} al principio de la ruta
@RestController
@RequestMapping("/{tenantId}/api/admin/profesionales")
@RequiredArgsConstructor
public class ProfesionalController {

    private final ProfesionalService profesionalService;

    // No necesitas añadir @PathVariable("tenantId") a cada método porque
    // tu JwtTenantFilter ya lo lee y lo pone en el TenantContext.
    // El @RequestMapping a nivel de clase es suficiente para que la ruta coincida.

    @PostMapping
    public ResponseEntity<ProfesionalDTO> createProfesional(@RequestBody ProfesionalCreateDTO createDTO) {
        ProfesionalDTO nuevoProfesional = profesionalService.createProfesional(createDTO);
        return new ResponseEntity<>(nuevoProfesional, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfesionalDTO> updateProfesional(@PathVariable Long id, @RequestBody ProfesionalUpdateDTO updateDTO) {
        ProfesionalDTO profesionalActualizado = profesionalService.updateProfesional(id, updateDTO);
        return ResponseEntity.ok(profesionalActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfesional(@PathVariable Long id) {
        profesionalService.deleteProfesional(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfesionalDTO> getProfesionalById(@PathVariable Long id) {
        return ResponseEntity.ok(profesionalService.findProfesionalById(id));
    }

    @GetMapping
    public ResponseEntity<List<ProfesionalDTO>> getAllProfesionales() {
        return ResponseEntity.ok(profesionalService.findAllProfesionales());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ProfesionalDTO> getProfesionalByEmail(@PathVariable String email) {
        return ResponseEntity.ok(profesionalService.findProfesionalByEmail(email));
    }
}