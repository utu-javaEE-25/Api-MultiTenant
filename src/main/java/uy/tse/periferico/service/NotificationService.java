package uy.tse.periferico.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uy.tse.periferico.dto.NotificationListResponse;
import uy.tse.periferico.dto.NotificationResponse;
import uy.tse.periferico.dto.NotificationReadRequest;
import uy.tse.periferico.model.Notificacion;
import uy.tse.periferico.model.PreferenciasNotificacion;
import uy.tse.periferico.model.Profesional;
import uy.tse.periferico.repository.NotificacionRepository;
import uy.tse.periferico.repository.PreferenciasNotificacionRepository;
import uy.tse.periferico.repository.ProfesionalRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificacionRepository notificacionRepo;
    private final PreferenciasNotificacionRepository prefRepo;
    private final ProfesionalRepository profesionalRepo;
    private final NotificationPushService pushService;

    public void crearYEnviarNotificacion(
            String username,
            String titulo,
            String mensaje,
            String tipo,
            String metadataJson) {
        Profesional profesional = profesionalRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Profesional no encontrado: " + username));

        // 1. Preferencias
        PreferenciasNotificacion prefs = prefRepo.findByProfesionalId(profesional.getId())
                .orElseGet(() -> crearPreferenciasPorDefecto(profesional));

        if (!prefs.isRecibirTodas() &&
                ((tipo.equals("SOLICITUD_ACCESO") && !prefs.isRecibirSolicitudesAcceso())
                        || (tipo.equals("ACCESO_HC") && !prefs.isRecibirAccesosHistoriaClinica()))) {
            return; // no enviar según preferencia
        }

        // 2. Persistir
        Notificacion n = new Notificacion();
        n.setProfesional(profesional);
        n.setTitulo(titulo);
        n.setMensaje(mensaje);
        n.setTipo(tipo);
        n.setMetadataJson(metadataJson);
        n.setFechaCreacion(LocalDateTime.now());
        notificacionRepo.save(n);

        // 3. Enviar push
        pushService.enviarPush(profesional, titulo, mensaje, tipo, metadataJson);
    }

    private PreferenciasNotificacion crearPreferenciasPorDefecto(Profesional p) {
        PreferenciasNotificacion pref = new PreferenciasNotificacion();
        pref.setProfesional(p);
        pref.setRecibirTodas(true);
        pref.setRecibirAccesosHistoriaClinica(true);
        pref.setRecibirSolicitudesAcceso(true);
        return prefRepo.save(pref);
    }

    public NotificationListResponse listarNotificaciones(String username) {
        Profesional profesional = profesionalRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Profesional no encontrado: " + username));

        List<Notificacion> lista = notificacionRepo.findByProfesionalIdOrderByFechaCreacionDesc(profesional.getId());

        NotificationListResponse resp = new NotificationListResponse();
        resp.setNotificaciones(lista.stream().map(this::mapToDTO).collect(Collectors.toList()));

        return resp;
    }

    public void marcarLeida(String username, Long id, NotificationReadRequest req) {
        Notificacion n = notificacionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada: " + id));

        if (req.isLeida() && !n.isLeida()) {
            n.setLeida(true);
            n.setFechaLectura(LocalDateTime.now());
            notificacionRepo.save(n);
        }
    }

    private NotificationResponse mapToDTO(Notificacion n) {
        NotificationResponse dto = new NotificationResponse();
        dto.setId(n.getId());
        dto.setTitulo(n.getTitulo());
        dto.setMensaje(n.getMensaje());
        dto.setTipo(n.getTipo());
        dto.setMetadataJson(n.getMetadataJson());
        dto.setFechaCreacion(n.getFechaCreacion().toString());
        dto.setLeida(n.isLeida());
        dto.setFechaLectura(n.getFechaLectura() != null ? n.getFechaLectura().toString() : null);
        return dto;
    }
}
