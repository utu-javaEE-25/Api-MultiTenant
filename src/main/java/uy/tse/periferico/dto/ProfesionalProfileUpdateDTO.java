package uy.tse.periferico.dto;

import lombok.Data;

@Data
public class ProfesionalProfileUpdateDTO {
    // Campos que el propio profesional puede cambiar
    private String nombre;
    private String apellido;
    private String especializacion;
    private String email;
    private String password;
}