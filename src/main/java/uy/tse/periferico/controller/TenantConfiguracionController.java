package uy.tse.periferico.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uy.tse.periferico.config.TenantContext;
import uy.tse.periferico.dto.TenantConfigUpdateDTO;
import uy.tse.periferico.model.TenantConfiguracion;
import uy.tse.periferico.service.TenantConfiguracionService;

@RestController
@RequestMapping("/{tenantId}/api") // CAMBIO 1: La ruta base ahora es más genérica
@RequiredArgsConstructor
public class TenantConfiguracionController {

    private final TenantConfiguracionService configService;

    // La ruta final es: /{tenantId}/api/config
    @GetMapping("/config")
    public ResponseEntity<TenantConfiguracion> getTenantConfig(@PathVariable String tenantId) {
        TenantContext.setCurrentTenant(tenantId);
        try {
            TenantConfiguracion config = configService.getConfiguration();
            return ResponseEntity.ok(config);
        } finally {
            TenantContext.clear();
        }
    }

    // CAMBIO 2: La ruta del método PUT ahora coincide con /admin/config
    // La ruta final es: /{tenantId}/api/admin/config <-- ¡CORRECTA!
    @PutMapping("/admin/config")
    public ResponseEntity<TenantConfiguracion> updateTenantConfig(
            @PathVariable String tenantId,
            @RequestBody TenantConfigUpdateDTO updateDTO) {

        TenantContext.setCurrentTenant(tenantId);
        try {
            TenantConfiguracion updatedConfig = configService.updateConfiguration(updateDTO);
            return ResponseEntity.ok(updatedConfig);
        } finally {
            TenantContext.clear();
        }
    }
}