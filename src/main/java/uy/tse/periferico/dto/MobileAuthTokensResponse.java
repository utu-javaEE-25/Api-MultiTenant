package uy.tse.periferico.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MobileAuthTokensResponse {

    private String token;
    private ProfesionalDTO profesional;
}
