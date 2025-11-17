// RUTA: src/main/java/uy/tse/periferico/dto/PacienteCreateDTO.java
package uy.tse.periferico.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PacienteCreateDTO {
    private String nombre;
    private String apellido;
    private String nroDocumento;
    private String sexo;
    private LocalDate fechaNacimiento;
    private String email;
}