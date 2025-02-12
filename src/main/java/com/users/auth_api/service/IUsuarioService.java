package com.users.auth_api.service;

import com.users.auth_api.dto.request.ActualizarUsuarioRequestDTO;
import com.users.auth_api.dto.response.UsuarioResponseDTO;
import com.users.auth_api.util.Result;

public interface IUsuarioService {

    Result<String, String> actualizarUsuario(Long id, ActualizarUsuarioRequestDTO request);
    Result<String, String> inactivarUsuario(Long id);
    Result<UsuarioResponseDTO, String> consultarUsuario(Long id);
}
