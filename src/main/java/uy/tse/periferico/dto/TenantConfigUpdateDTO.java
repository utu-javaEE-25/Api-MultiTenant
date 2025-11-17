package uy.tse.periferico.dto;

import lombok.Data;

// Esta clase es un molde para los datos que vienen del formulario de React.
// Solo contiene los campos que el administrador puede modificar.
@Data
public class TenantConfigUpdateDTO {
    private String tituloPrincipal;
    private String colorPrimario;
    private String colorFondo;
    private String logoUrl; // Aquí llegará el string Base64 de la imagen
}