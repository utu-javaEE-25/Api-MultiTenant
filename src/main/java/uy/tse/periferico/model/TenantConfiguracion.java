package uy.tse.periferico.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "configuracion_tenant")
@Data
public class TenantConfiguracion {
    @Id
    private Integer id;
    private String logoUrl;
    private String tituloPrincipal;
    private String colorPrimario;
    private String colorFondo;
}
