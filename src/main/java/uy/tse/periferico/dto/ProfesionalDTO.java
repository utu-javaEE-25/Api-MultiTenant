// RUTA: src/main/java/uy/tse/periferico/dto/ProfesionalDTO.java
package uy.tse.periferico.dto;

import lombok.Data;

@Data
public class ProfesionalDTO {
    private Long id;
    private String username;
    private String nombre;
    private String apellido;
    private String especializacion;
    private String rol;
    private String email;
    private String estado; // <-- Usamos String para el estado
}