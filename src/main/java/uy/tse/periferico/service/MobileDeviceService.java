package uy.tse.periferico.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import uy.tse.periferico.dto.MobileDeviceResponse;
import uy.tse.periferico.dto.RegisterDeviceRequest;
import uy.tse.periferico.model.MobileDeviceToken;
import uy.tse.periferico.model.Profesional;
import uy.tse.periferico.repository.MobileDeviceTokenRepository;
import uy.tse.periferico.repository.ProfesionalRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MobileDeviceService {

    private final MobileDeviceTokenRepository deviceRepo;
    private final ProfesionalRepository profesionalRepository;

    public MobileDeviceResponse registrarDispositivo(String username, RegisterDeviceRequest req) {

        Profesional profesional = profesionalRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Profesional no encontrado: " + username));

        // Si el device ya existe → lo reemplazamos (mismo deviceId)
        deviceRepo.deleteByDeviceId(req.getDeviceId());

        MobileDeviceToken device = new MobileDeviceToken();
        device.setDeviceId(req.getDeviceId());
        device.setFcmToken(req.getFcmToken());
        device.setPlataforma(req.getPlataforma());
        device.setDescripcion(req.getDescripcion());
        device.setProfesional(profesional);
        device.setFechaRegistro(LocalDateTime.now());

        MobileDeviceToken saved = deviceRepo.save(device);

        MobileDeviceResponse resp = new MobileDeviceResponse();
        resp.setId(saved.getId());
        resp.setDeviceId(saved.getDeviceId());
        resp.setPlataforma(saved.getPlataforma());
        resp.setDescripcion(saved.getDescripcion());
        resp.setFechaRegistro(saved.getFechaRegistro().toString());

        return resp;
    }

    public List<MobileDeviceResponse> listarDispositivos(String username) {

        Profesional profesional = profesionalRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Profesional no encontrado: " + username));

        return deviceRepo.findByProfesionalId(profesional.getId())
                .stream()
                .map(dev -> {
                    MobileDeviceResponse r = new MobileDeviceResponse();
                    r.setId(dev.getId());
                    r.setDeviceId(dev.getDeviceId());
                    r.setPlataforma(dev.getPlataforma());
                    r.setDescripcion(dev.getDescripcion());
                    r.setFechaRegistro(dev.getFechaRegistro().toString());
                    return r;
                })
                .collect(Collectors.toList());
    }

    public void eliminarDispositivo(String username, Long id) {

        Profesional profesional = profesionalRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Profesional no encontrado: " + username));

        deviceRepo.findById(id).ifPresent(dev -> {
            if (dev.getProfesional().getId().equals(profesional.getId())) {
                deviceRepo.deleteById(id);
            }
        });
    }
}
