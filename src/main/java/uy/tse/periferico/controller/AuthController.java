package uy.tse.periferico.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import uy.tse.periferico.dto.LoginRequest;
import uy.tse.periferico.dto.LoginResponse;
import uy.tse.periferico.dto.ProfesionalDTO;
import uy.tse.periferico.dto.ProfesionalProfileUpdateDTO;
import uy.tse.periferico.dto.MobileLoginResponse;

import uy.tse.periferico.model.Paciente;

import uy.tse.periferico.service.AutenticacionService;
import uy.tse.periferico.service.ProfesionalService;
import uy.tse.periferico.service.ImportacionPacienteService;
import uy.tse.periferico.service.MobileAuthService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/{tenantId}/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AutenticacionService autenticacionService;
    private final ProfesionalService profesionalService;
    private final ImportacionPacienteService importacionService;
    private final MobileAuthService mobileAuthService;

    // LOGIN PROFESIONAL (WEB Y MOBILE)
    @PostMapping("/login/profesional")
    public ResponseEntity<?> loginProfesional(
            @PathVariable String tenantId,
            @RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();

        // Login normal → devuelve token
        String token = autenticacionService.loginProfesional(loginRequest, tenantId);

        // Si viene de MOBILE → devolvemos mobileAuthCode (no token directo)
        if ("mobile".equalsIgnoreCase(loginRequest.getOrigin())) {
            String mobileCode = mobileAuthService.generateMobileAuthCode(username);
            return ResponseEntity.ok(new MobileLoginResponse(mobileCode));
        }

        // Login WEB normal
        return ResponseEntity.ok(new LoginResponse(token));
    }

    // LOGIN ADMIN
    @PostMapping("/login/admin")
    public ResponseEntity<LoginResponse> loginAdmin(
            @PathVariable String tenantId,
            @RequestBody LoginRequest loginRequest) {
        String token = autenticacionService.loginAdmin(loginRequest, tenantId);
        return ResponseEntity.ok(new LoginResponse(token));
    }

    // ACTUALIZAR PERFIL
    @PutMapping("/perfil")
    public ResponseEntity<ProfesionalDTO> updateOwnProfile(
            @AuthenticationPrincipal String username,
            @RequestBody ProfesionalProfileUpdateDTO updateDTO) {
        ProfesionalDTO profesionalActualizado = profesionalService.updateOwnProfile(username, updateDTO);
        return ResponseEntity.ok(profesionalActualizado);
    }

    // VER PERFIL
    @GetMapping("/perfil")
    public ResponseEntity<ProfesionalDTO> getOwnProfile(
            @AuthenticationPrincipal String username) {
        ProfesionalDTO profesional = profesionalService.getProfileByUsername(username);
        return ResponseEntity.ok(profesional);
    }

    // IMPORTAR PACIENTE
    @PostMapping("/admin/importar-paciente/{cedula}")
    public ResponseEntity<?> importarPaciente(
            @PathVariable String tenantId,
            @PathVariable String cedula) {
        try {
            Paciente pacienteImportado = importacionService.importarPacientePorCI(cedula);
            return new ResponseEntity<>(pacienteImportado, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
