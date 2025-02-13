package com.users.auth_api.dto.response;

import com.users.auth_api.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponseDTO {

    private String numeroIdentificacion;
    private String nombre;
    private String apellido;
    private String telefono;
    private String correo;
    private String contrasena;
    private boolean activo;
    private Set<Roles> roles;

}
