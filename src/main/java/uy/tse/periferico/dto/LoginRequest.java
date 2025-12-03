package uy.tse.periferico.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;

    // Campo para distinguir login WEB vs MOBILE
    private String origin;
}
