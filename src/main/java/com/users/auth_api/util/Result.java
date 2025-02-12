package com.users.auth_api.util;

import lombok.*;
import org.springframework.http.HttpStatus;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Result<R, T> {
    private final R value;
    private final boolean isSuccess;
    private final List<T> errors;
    private final HttpStatus statusCode;


    // Método estático para éxito
    public static <R, T> Result<R, T> success(R resultValue) {
        return new Result<R, T>(resultValue, true, new ArrayList<>(), HttpStatus.OK);
    }

    // Método estático para error
    public static <R, T> Result<R, T> failure(List<T> errors, HttpStatus statusCode) {
        return new Result<R, T>(null, false, errors, statusCode);
    }

}
