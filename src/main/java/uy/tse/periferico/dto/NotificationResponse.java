package uy.tse.periferico.dto;

import lombok.Data;

@Data
public class NotificationResponse {

    private Long id;
    private String titulo;
    private String mensaje;
    private String tipo;
    private String metadataJson;
    private String fechaCreacion;
    private boolean leida;
    private String fechaLectura;
}
