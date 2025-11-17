package uy.tse.periferico.dto;

import lombok.Data;

@Data
public class SolicitudAccesoRequestDTO {
    private String schemaTenantSolicitante;
    private String cedulaPaciente;
    private String idExternaDoc;
    private String motivo;
    private Long idProfesionalSolicitante;
    private String nombreProfesionalSolicitante;
}