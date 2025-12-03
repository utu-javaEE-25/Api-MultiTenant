package uy.tse.periferico.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uy.tse.periferico.dto.MobileAuthCodeRequest;
import uy.tse.periferico.dto.MobileAuthTokensResponse;
import uy.tse.periferico.service.MobileAuthService;

@RestController
@RequestMapping("/{tenantId}/api/mobile/auth")
@RequiredArgsConstructor
public class MobileAuthController {

    private final MobileAuthService mobileAuthService;

    /**
     * Devuelve la URL que la app debe abrir para iniciar el login móvil.
     */
    @GetMapping("/login-url")
    public ResponseEntity<String> getLoginUrl(@PathVariable String tenantId) {
        String url = mobileAuthService.buildMobileLoginUrl(tenantId);
        return ResponseEntity.ok(url);
    }

    /**
     * Intercambia mobileAuthCode -> tokens reales del backend.
     */
    @PostMapping("/exchange")
    public ResponseEntity<MobileAuthTokensResponse> exchange(
            @PathVariable String tenantId,
            @RequestBody MobileAuthCodeRequest request) {
        MobileAuthTokensResponse tokens = mobileAuthService.exchangeMobileAuthCode(request.getCode(), tenantId);
        return ResponseEntity.ok(tokens);
    }
}
