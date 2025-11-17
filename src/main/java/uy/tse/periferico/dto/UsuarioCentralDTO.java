package uy.tse.periferico.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UsuarioCentralDTO {

    private Long id;
    private String cedulaIdentidad;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private String email; // <--- CAMBIO: Campo añadido

    // --- Getters y Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCedulaIdentidad() { return cedulaIdentidad; }
    public void setCedulaIdentidad(String cedulaIdentidad) { this.cedulaIdentidad = cedulaIdentidad; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public String getEmail() { return email; } // <--- CAMBIO: Getter y Setter añadidos
    public void setEmail(String email) { this.email = email; }
}