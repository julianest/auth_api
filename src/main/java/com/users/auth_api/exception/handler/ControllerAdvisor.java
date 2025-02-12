package com.users.auth_api.exception.handler;


import com.users.auth_api.dto.response.ResponseDTO;
import com.users.auth_api.util.Constants;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.MissingRequestValueException;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO<Object>> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errorMessages = new ArrayList<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            if (error instanceof FieldError fieldError) {
                errorMessages.add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
            } else {
                errorMessages.add(error.getDefaultMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDTO.builder()
                .message(Constants.MESSAGE_ERROR_BODY)
                .code(HttpStatus.BAD_REQUEST.value()).response(errorMessages).build());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseDTO<Object>> handleIlegalException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDTO.builder()
                .message(Constants.MESSAGE_ERROR_BODY)
                .code(HttpStatus.BAD_REQUEST.value()).response(ex.getMessage()).build());
    }

    @ExceptionHandler(MissingRequestValueException.class)
    public ResponseEntity<ResponseDTO<Object>> handleMissingRequestValueException(MissingRequestValueException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDTO.builder()
                .message(Constants.MESSAGE_ERROR_BODY)
                .code(HttpStatus.BAD_REQUEST.value()).response(ex.getMessage()).build());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ResponseDTO<Object>> handleExpiredJwtException(ExpiredJwtException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDTO.builder()
                .message(Constants.MESSAGE_ERROR_BODY)
                .code(HttpStatus.BAD_REQUEST.value()).response(ex.getMessage()).build());
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ResponseDTO<Object>> handleSignatureException(SignatureException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDTO.builder()
                .message(Constants.MESSAGE_ERROR_BODY)
                .code(HttpStatus.BAD_REQUEST.value()).response(ex.getMessage()).build());
    }

}
