package com.users.auth_api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEventMessage implements Serializable {
    private static final long serialVersionUID = 1l;

    private String eventType; // "REGISTER", "LOGIN", "LOGOUT"
    private Long userId;
    private String numberIdentification;
    private String correo;

}
