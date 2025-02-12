package com.users.auth_api.mapper;

import com.users.auth_api.dto.request.RegistrarUsuarioRequestDTO;
import com.users.auth_api.dto.response.UsuarioResponseDTO;
import com.users.auth_api.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IUsuarioMapper {

    Usuario toUsuario(RegistrarUsuarioRequestDTO registrarUsuarioRequestDTO);
    
    UsuarioResponseDTO toResponseDTO(Usuario usuario);

}
