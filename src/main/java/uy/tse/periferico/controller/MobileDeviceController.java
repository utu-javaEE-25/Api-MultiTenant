package uy.tse.periferico.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import uy.tse.periferico.dto.RegisterDeviceRequest;
import uy.tse.periferico.dto.MobileDeviceResponse;
import uy.tse.periferico.service.MobileDeviceService;

import java.util.List;

@RestController
@RequestMapping("/{tenantId}/api/mobile/devices")
@RequiredArgsConstructor
public class MobileDeviceController {

    private final MobileDeviceService mobileDeviceService;

    @PostMapping
    public ResponseEntity<MobileDeviceResponse> registrar(
            @AuthenticationPrincipal String username,
            @RequestBody RegisterDeviceRequest req) {
        return ResponseEntity.ok(mobileDeviceService.registrarDispositivo(username, req));
    }

    @GetMapping
    public ResponseEntity<List<MobileDeviceResponse>> listar(
            @AuthenticationPrincipal String username) {
        return ResponseEntity.ok(mobileDeviceService.listarDispositivos(username));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @AuthenticationPrincipal String username,
            @PathVariable Long id) {
        mobileDeviceService.eliminarDispositivo(username, id);
        return ResponseEntity.noContent().build();
    }
}
