package uy.tse.periferico.dto;

import lombok.Data;

@Data
public class MobileDeviceResponse {

    private Long id;
    private String deviceId;
    private String plataforma;
    private String descripcion;
    private String fechaRegistro;
}
