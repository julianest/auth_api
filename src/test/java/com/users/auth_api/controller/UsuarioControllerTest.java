package com.users.auth_api.controller;

import com.users.auth_api.dto.request.ActualizarUsuarioRequestDTO;
import com.users.auth_api.dto.response.ResponseDTO;
import com.users.auth_api.dto.response.UsuarioResponseDTO;
import com.users.auth_api.service.IUsuarioService;
import com.users.auth_api.util.Result;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private IUsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    @Test
    void actualizarUsuario_CuandoEsExitoso_RetornaOk() {

        Long id = 1L;
        ActualizarUsuarioRequestDTO request = new ActualizarUsuarioRequestDTO();
        Result<String, String> result = Result.success("Usuario actualizado exitosamente");
        when(usuarioService.actualizarUsuario(id, request)).thenReturn(result);

        ResponseEntity<ResponseDTO<String>> response = usuarioController.actualizarUsuario(id, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Usuario actualizado exitosamente",response.getBody().getMessage());
        assertEquals(HttpStatus.OK.value(), response.getBody().getCode());
    }

    @Test
    void inactivarUsuario_CuandoEsExitoso_RetornaOk() {
        Long id = 1L;
        Result<String, String> result = Result.success("Usuario inactivado exitosamente");
        when(usuarioService.inactivarUsuario(id)).thenReturn(result);

        ResponseEntity<ResponseDTO<String>> response = usuarioController.inactivarUsuario(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Usuario inactivado exitosamente", response.getBody().getMessage());
        assertEquals(HttpStatus.OK.value(), response.getBody().getCode());
    }

    @Test
    void consultarUsuario_CuandoEsExitoso_RetornaOk() {
        Long id = 1L;
        UsuarioResponseDTO usuarioResponseDTO = new UsuarioResponseDTO();
        Result<UsuarioResponseDTO, String> result = Result.success(usuarioResponseDTO);
        when(usuarioService.consultarUsuario(id)).thenReturn(result);

        ResponseEntity<ResponseDTO<?>> response = usuarioController.consultarUsuario(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Se ha consultado el usuario satisfactoriamente.", response.getBody().getMessage());
        assertEquals(HttpStatus.OK.value(), response.getBody().getCode());
        assertEquals(usuarioResponseDTO, response.getBody().getResponse());
    }

    @Test
    void consultarUsuario_CuandoFalla_RetornaBadRequest() {
        Long id = 1L;
        Result<UsuarioResponseDTO, String> result = Result.failure(List.of("Error al consultar usuario"), HttpStatus.BAD_REQUEST);
        when(usuarioService.consultarUsuario(id)).thenReturn(result);

        ResponseEntity<ResponseDTO<?>> response = usuarioController.consultarUsuario(id);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error al consultar usuario", response.getBody().getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getCode());
        assertEquals(null, response.getBody().getResponse());
    }

}
