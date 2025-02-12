package com.users.auth_api.service.impl;

import com.users.auth_api.dto.request.ActualizarUsuarioRequestDTO;
import com.users.auth_api.dto.response.UsuarioResponseDTO;
import com.users.auth_api.entity.Usuario;
import com.users.auth_api.mapper.IUsuarioMapper;
import com.users.auth_api.repository.IUsuarioRepository;
import com.users.auth_api.service.IUsuarioService;
import com.users.auth_api.util.Result;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UsuarioServiceImpl implements IUsuarioService{

    private final IUsuarioRepository usuarioRepository;
    private final IUsuarioMapper usuarioMapper;
	
    @Override
    public Result<String, String> actualizarUsuario(Long id, ActualizarUsuarioRequestDTO request) {
        Optional<Usuario> usuario = usuarioRepository.findByIdAndActivoTrue(id);
        if(!usuario.isPresent()) {
            return Result.failure(List.of("Usuario no encontrado"), HttpStatus.BAD_REQUEST);
        }
        if(request.getNombre() != null) {
            usuario.get().setNombre(request.getNombre());
        }
        if(request.getApellido() != null) {
            usuario.get().setApellido(request.getApellido());
        }
        if(request.getCorreo() != null) {
            usuario.get().setCorreo(request.getCorreo());
        }
        if(request.getTelefono() != null) {
            usuario.get().setTelefono(request.getTelefono());
        }
        usuarioRepository.save(usuario.get());
        return Result.success("Usuario actualizado correctamente");
    }


    @Override
    public Result<String, String> inactivarUsuario(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findByIdAndActivoTrue(id);
        if(!usuario.isPresent()) {
            return Result.failure(List.of("Usuario no encontrado"), HttpStatus.BAD_REQUEST);
        }
        usuario.get().setActivo(false);
        usuarioRepository.save(usuario.get());
        return Result.success("Usuario inactivado correctamente");
    }


    @Override
    public Result<UsuarioResponseDTO, String> consultarUsuario(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findByIdAndActivoTrue(id);
        if(!usuario.isPresent()) {
            return Result.failure(List.of("Usuario no encontrado"), HttpStatus.BAD_REQUEST);
        }
        return Result.success(usuarioMapper.toResponseDTO(usuario.get()));
    }

}
