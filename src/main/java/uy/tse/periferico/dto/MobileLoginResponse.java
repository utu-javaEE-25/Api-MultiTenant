package uy.tse.periferico.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MobileLoginResponse {
    private String mobileAuthCode;
}
