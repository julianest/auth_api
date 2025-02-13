package com.users.auth_api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class JmsMessageService {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void sendEvent(String appMessage, Object message) {
        try {
            jmsTemplate.convertAndSend("auth-queue", message, msg -> {
                msg.setStringProperty("appMessage", appMessage);
                msg.setStringProperty("_type", message.getClass().getName()); // Tipo dinámico del mensaje
                return msg;
            });

        } catch (Exception e) {
            System.err.println("⚠️ Error enviando mensaje: " + e.getMessage());
        }
    }
}
