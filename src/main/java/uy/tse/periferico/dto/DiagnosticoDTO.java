package uy.tse.periferico.dto;

import lombok.Data;

@Data
public class DiagnosticoDTO {
    private String descripcion;
    private String fechaInicio;
    private String estadoProblema;
    private String gradoCerteza;
}