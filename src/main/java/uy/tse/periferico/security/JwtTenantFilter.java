package uy.tse.periferico.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uy.tse.periferico.config.TenantContext;
import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtTenantFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // Extraer el tenantId de la URL usando nuestro método auxiliar
        String tenantIdFromUrl = extractTenantIdFromRequest(request);

        // Si la URL no tiene el formato esperado (ej: /tenant/api/...), es una petición inválida.
        if (tenantIdFromUrl == null) {
            // Excepción: la URL de login NO necesita token pero SÍ tenant en la URL.
            filterChain.doFilter(request, response);
            return;
        }

        try {
            //Establecer el contexto del tenant INMEDIATAMENTE.
            TenantContext.setCurrentTenant(tenantIdFromUrl);
            
            final String authHeader = request.getHeader("Authorization");

            // Caso especial: la URL de login. No requiere token, así que la dejamos continuar.
            if (request.getRequestURI().endsWith("/api/auth/login")) {
                filterChain.doFilter(request, response);
                return;
            }
            
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response); 
                return;
            }

            final String token = authHeader.substring(7);
            
            Claims claims = jwtTokenProvider.validateAndGetClaims(token);
            String username = claims.getSubject();
            String tenantIdFromToken = claims.get("tenant_id", String.class);

            if (tenantIdFromToken == null || !tenantIdFromToken.equals(tenantIdFromUrl)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Acceso prohibido: El token no es válido para este tenant.");
                return;
            }
            
            String principal = (username != null) ? username : tenantIdFromToken;

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    principal, null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            filterChain.doFilter(request, response);

        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token JWT inválido o expirado.");
        } finally {
            TenantContext.clear();
        }
    }

    
    private String extractTenantIdFromRequest(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String[] parts = requestURI.split("/");
        
        if (parts.length > 2 && "api".equals(parts[2])) {
            return parts[1];
        }
        return null;
    }
}