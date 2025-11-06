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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority; 
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uy.tse.periferico.config.TenantContext;
import java.io.IOException;
import java.util.Collections;
import java.util.List; 

@Component
@RequiredArgsConstructor
public class JwtTenantFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String tenantIdFromUrl = extractTenantIdFromRequest(request);

        if (tenantIdFromUrl == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            TenantContext.setCurrentTenant(tenantIdFromUrl);
            
            final String authHeader = request.getHeader("Authorization");

            if (request.getRequestURI().contains("/api/auth/login")) { 
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
            
            String rol = claims.get("rol", String.class);

            List<GrantedAuthority> authorities = rol != null ?
                    List.of(new SimpleGrantedAuthority("ROLE_" + rol)) :
                    Collections.emptyList();

            if (tenantIdFromToken == null || !tenantIdFromToken.equals(tenantIdFromUrl)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Acceso prohibido: El token no es válido para este tenant.");
                return;
            }
            
            String principal = (username != null) ? username : tenantIdFromToken;

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    principal, null, authorities);
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