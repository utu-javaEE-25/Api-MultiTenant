package uy.tse.periferico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * ESTE ES UN COMPONENTE TEMPORAL DE UTILIDAD.
 * Su único propósito es generar un hash BCrypt al arrancar la aplicación.
 * Deberá ser eliminado o comentado después de usarlo.
 */
@Component
public class PasswordGenerator implements CommandLineRunner {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String rawPassword = "adminpass456";
        String hashedPassword = passwordEncoder.encode(rawPassword);

        System.out.println("====================================================================");
        System.out.println("NUEVO HASH BCrypt GARANTIZADO para 'password123':");
        System.out.println(hashedPassword);
        System.out.println("====================================================================");
    }
}