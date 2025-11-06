package uy.tse.periferico.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;

@Entity
@Table(name = "tenant_config")
@Data
public class TenantConfiguracion {

    @Id
    @Column(name = "config_id")
    private Long id;

    @Column(name = "logourl")
    private String logoUrl;

    @Column(name = "nombre_visible")
    private String tituloPrincipal;

    @Column(name = "color_principal")
    private String colorPrimario;

    @Column(name = "color_fondo")
    private String colorFondo;

    @Column(name = "email_contacto")
    private String emailContacto;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;
}