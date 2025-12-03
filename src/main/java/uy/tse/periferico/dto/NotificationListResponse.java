package uy.tse.periferico.dto;

import lombok.Data;

import java.util.List;

@Data
public class NotificationListResponse {

    private List<NotificationResponse> notificaciones;
}
