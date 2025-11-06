package uy.tse.periferico.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uy.tse.periferico.dto.LoginRequest;
import uy.tse.periferico.dto.LoginResponse;
import uy.tse.periferico.service.AutenticacionService;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/{tenantId}/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AutenticacionService autenticacionService;

    // Renombrado para mayor claridad (de /login a /login/profesional)
    @PostMapping("/login/profesional")
    public ResponseEntity<LoginResponse> loginProfesional(@PathVariable String tenantId, @RequestBody LoginRequest loginRequest) {
        String token = autenticacionService.loginProfesional(loginRequest, tenantId);
        return ResponseEntity.ok(new LoginResponse(token));
    }
    
    // --- NUEVO ENDPOINT PARA EL LOGIN DEL ADMINISTRADOR ---
    @PostMapping("/login/admin")
    public ResponseEntity<LoginResponse> loginAdmin(@PathVariable String tenantId, @RequestBody LoginRequest loginRequest) {
        // Llama a la nueva función del servicio
        String token = autenticacionService.loginAdmin(loginRequest, tenantId);
        // Devuelve el token en la respuesta
        return ResponseEntity.ok(new LoginResponse(token));
    }
}

