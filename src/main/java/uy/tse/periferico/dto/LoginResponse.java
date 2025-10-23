package uy.tse.periferico.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor 
public class LoginResponse {
// El token JWT que el frontend deberá guardar y usar en peticiones futuras.
private String token;
}
