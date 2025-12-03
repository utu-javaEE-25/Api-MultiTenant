package uy.tse.periferico.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "mobile_device_token")
@Data
public class MobileDeviceToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String deviceId;

    @Column(nullable = false)
    private String fcmToken;

    private String plataforma; // ANDROID / IOS
    private String descripcion; // info opcional del dispositivo

    private LocalDateTime fechaRegistro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profesional_id")
    private Profesional profesional;
}
