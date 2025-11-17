// RUTA: src/main/java/uy/tse/periferico/controller/AdminPacienteController.java
package uy.tse.periferico.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uy.tse.periferico.dto.PacienteCreateDTO;
import uy.tse.periferico.model.Paciente;
import uy.tse.periferico.service.PacienteService;

@RestController
// ¡Esta es la ruta correcta para las acciones de admin sobre pacientes!
@RequestMapping("/{tenantId}/api/admin/pacientes")
@RequiredArgsConstructor
public class AdminPacienteController {

    private final PacienteService pacienteService;

    // La ruta final será: POST /{tenantId}/api/admin/pacientes
    @PostMapping
    public ResponseEntity<Paciente> crearPacienteLocal(@RequestBody PacienteCreateDTO createDTO) {
        Paciente pacienteCreado = pacienteService.crearPacienteLocal(createDTO);
        return new ResponseEntity<>(pacienteCreado, HttpStatus.CREATED);
    }
}