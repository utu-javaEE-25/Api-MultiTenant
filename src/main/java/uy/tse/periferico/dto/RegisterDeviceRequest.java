package uy.tse.periferico.dto;

import lombok.Data;

@Data
public class RegisterDeviceRequest {

    private String deviceId; // ID único generado por la app
    private String fcmToken; // token firebase
    private String plataforma; // "ANDROID" o "IOS"
    private String descripcion; // opcional
}
