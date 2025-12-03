package uy.tse.periferico.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "preferencias_notificacion")
@Data
public class PreferenciasNotificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación 1:1
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profesional_id", nullable = false)
    private Profesional profesional;

    // Preferencias simples (extendibles)
    private boolean recibirTodas = true;
    private boolean recibirSolicitudesAcceso = true;
    private boolean recibirAccesosHistoriaClinica = true;
}
