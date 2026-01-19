package dev.luchonetvv.userapi.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ErrorMessageResponse", description = "Respuesta estándar de error")
public class ErrorMessageResponse {
    @Schema(example = "El correo ya registrado", description = "Mensaje de error")
    private String message;

    public ErrorMessageResponse() {}

    public ErrorMessageResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
