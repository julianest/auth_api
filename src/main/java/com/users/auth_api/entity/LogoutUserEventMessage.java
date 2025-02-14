package com.users.auth_api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogoutUserEventMessage implements Serializable {
    private static final long serialVersionUID = 1l;

    private String eventType;
    private String token;

}
