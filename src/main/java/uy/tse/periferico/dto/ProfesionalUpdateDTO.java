// RUTA: src/main/java/uy/tse/periferico/dto/ProfesionalUpdateDTO.java
package uy.tse.periferico.dto;

import lombok.Data;

@Data
public class ProfesionalUpdateDTO {
    private String nombre;
    private String apellido;
    private String especializacion;
    private String password; // Opcional: para cambiar la contraseña
}