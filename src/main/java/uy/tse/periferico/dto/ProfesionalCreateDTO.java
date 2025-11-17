// RUTA: src/main/java/uy/tse/periferico/dto/ProfesionalCreateDTO.java
package uy.tse.periferico.dto;

import lombok.Data;

@Data
public class ProfesionalCreateDTO {
    private String username;
    private String password; // Contraseña en texto plano
    private String nombre;
    private String apellido;
    private String especializacion;
    private String email;
    private String estado;

}