package uy.tse.periferico.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "profesional", uniqueConstraints = { 
    @UniqueConstraint(columnNames = {"nombre_usuario"}) 
})
@Data
@NoArgsConstructor
public class Profesional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profesional_id") 
    private Long id;

    @Column(name = "nombre_usuario", nullable = false, unique = true)
    private String username; 
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    private String especializacion;
    private String email;
    @Column(nullable = false)
    private String estado = "ACTIVO";
}
