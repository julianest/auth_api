package com.users.auth_api.entity;

import java.io.Serializable;

public class SystemMessage implements Serializable {
    private static final long serialVersionUID = 1l;

    private String source;
    private String message;

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }


    @Override
    public String toString() {
        return "SystemMessage{" +
                "source='" + source + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
