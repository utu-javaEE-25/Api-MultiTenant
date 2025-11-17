package uy.tse.periferico.config;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

import uy.tse.periferico.security.JwtTenantFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTenantFilter jwtTenantFilter;

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        // --- PASO 1: Definir las rutas públicas PRIMERO ---
                        .requestMatchers("/{tenantId}/api/auth/login/**", "/{tenantId}/api/config").permitAll()

                        // --- PASO 2: Definir las rutas de ADMIN ---
                        // Usamos hasRole("ADMIN") por consistencia. Es igual a hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/{tenantId}/api/admin/**").hasRole("ADMIN")

                        // --- PASO 3: Definir las rutas de PROFESIONAL ---
                        // Ruta para que el profesional edite su propio perfil
                        .requestMatchers("/{tenantId}/api/auth/perfil").hasRole("PROFESIONAL")

                        // Ruta para que el profesional vea documentos
                        .requestMatchers("/{tenantId}/api/pacientes/**").hasRole("PROFESIONAL")
                        .requestMatchers("/{tenantId}/api/documentos/**").hasAnyRole("PROFESIONAL", "SYSTEM")

                        // --- PASO 4: La regla más general, AL FINAL ---
                        // Cualquier otra petición que no haya coincidido antes, requiere autenticación.
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtTenantFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 1. Orígenes permitidos (tu frontend)
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedOrigins(Arrays.asList("https://front-multitenant.vercel.app"));

        // 2. Métodos permitidos
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // 3. Cabeceras permitidas (usar "*" es más seguro para desarrollo)
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // 4. Permitir credenciales (importante para tokens y cookies)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

