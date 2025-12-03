package uy.tse.periferico.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uy.tse.periferico.model.MobileDeviceToken;
import uy.tse.periferico.model.Profesional;
import uy.tse.periferico.repository.MobileDeviceTokenRepository;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationPushService {

    private final MobileDeviceTokenRepository deviceRepo;

    @Value("${fcm.server.key}")
    private String fcmServerKey;

    private static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";

    public void enviarPush(
            Profesional profesional,
            String titulo,
            String mensaje,
            String tipo,
            String metadataJson) {

        List<MobileDeviceToken> dispositivos = deviceRepo.findByProfesionalId(profesional.getId());

        for (MobileDeviceToken dev : dispositivos) {
            try {
                enviarMensajeFcm(dev.getFcmToken(), titulo, mensaje, tipo, metadataJson);
            } catch (Exception ignored) {
            }
        }
    }

    private void enviarMensajeFcm(String token, String titulo, String mensaje, String tipo, String metadataJson)
            throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> body = Map.of(
                "to", token,
                "notification", Map.of(
                        "title", titulo,
                        "body", mensaje),
                "data", Map.of(
                        "tipo", tipo,
                        "metadata", metadataJson));

        String json = mapper.writeValueAsString(body);

        URL url = new URL(FCM_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "key=" + fcmServerKey);
        conn.setRequestProperty("Content-Type", "application/json");

        conn.getOutputStream().write(json.getBytes(StandardCharsets.UTF_8));
        conn.getInputStream().close(); // dispara la request
    }
}
