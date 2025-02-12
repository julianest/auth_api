package com.users.auth_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrarUsuarioRequestDTO {
	
    @Schema(description = "Numero de identificacion del usuario", example = "12346", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotBlank(message = "El campo 'numeroIdetificacion' es obligatorio")
    private String numeroIdetificacion;
    @Schema(description = "Primer nombre del usuario", example = "Carlos", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El campo 'nombre' es obligatorio")
    private String nombre;
    @Schema(description = "apellido del usuario", example = "Sanchez", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El campo 'apellido' es obligatorio")
    private String apellido;
    @Schema(description = "telefono del usuario", example = "3003001234", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El campo 'telefono' es obligatorio")
    private String telefono;
    @Schema(description = "Correo del usuario", example = "carlos@gmail.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El campo 'correo' es obligatorio")
    @Email(message = "El correo debe ser válido")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.(com)$", message = "El correo debe ser válido y terminar en .com")
    private String correo;
    @Schema(description = "Contraseña de ingreso al aplicativo", example = "*******", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El campo 'contrasena' es obligatorio")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "La contraseña debe tener al menos 8 caracteres, incluir letras, números y un carácter especial"
    )
    private String contrasena;

}
