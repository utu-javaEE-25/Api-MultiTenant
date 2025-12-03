package uy.tse.periferico.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import uy.tse.periferico.dto.NotificationListResponse;
import uy.tse.periferico.dto.NotificationReadRequest;
import uy.tse.periferico.service.NotificationService;

@RestController
@RequestMapping("/{tenantId}/api/mobile/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<NotificationListResponse> listar(
            @AuthenticationPrincipal String username) {
        return ResponseEntity.ok(notificationService.listarNotificaciones(username));
    }

    @PostMapping("/{id}/read")
    public ResponseEntity<Void> marcarLeida(
            @AuthenticationPrincipal String username,
            @PathVariable Long id,
            @RequestBody NotificationReadRequest req) {
        notificationService.marcarLeida(username, id, req);
        return ResponseEntity.noContent().build();
    }
}
