// RUTA: src/main/java/uy/tse/periferico/controller/PacienteController.java
package uy.tse.periferico.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uy.tse.periferico.model.Paciente;
import uy.tse.periferico.service.PacienteService;
import uy.tse.periferico.dto.SolicitudAccesoDTO;
// ¡Asegúrate de ELIMINAR el import de PacienteCreateDTO si ya no se usa aquí!

import java.util.List;

@RestController
@RequestMapping("/{tenantId}/api/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService pacienteService;

    @GetMapping("/search")
    public ResponseEntity<List<Paciente>> searchPacientesByNroDocumento(@RequestParam("nroDocumento") String nroDocumento) {
        return ResponseEntity.ok(pacienteService.findByNroDocumento(nroDocumento));
    }

    @PostMapping("/solicitar-acceso")
    public ResponseEntity<String> solicitarAccesoPaciente(
            @AuthenticationPrincipal String cedulaProfesional,
            @RequestBody SolicitudAccesoDTO solicitud) {
        try {
            String mensaje = pacienteService.solicitarAcceso(cedulaProfesional, solicitud.getCedulaPaciente());
            return ResponseEntity.ok(mensaje);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // EL MÉTODO POST PARA ADMIN SE HA MOVIDO A AdminPacienteController
}