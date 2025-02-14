package com.users.auth_api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.MessageConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class JmsMessageService {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageConsumer.class);

    public void sendEvent(String appMessage,String eventType, Object message) {
        String typeActiveMQ;
        if( eventType == "LOGOUT"){
            typeActiveMQ = "auth-topic";
            sendEventClasified(typeActiveMQ,appMessage, message);
        }else{
            typeActiveMQ = "auth-queue";
            sendEventClasified(typeActiveMQ,appMessage, message);
        }
    }

    public void sendEventClasified(String typeActiveMQ, String appMessage, Object message){
        try {

            jmsTemplate.convertAndSend(typeActiveMQ, message, msg -> {
                msg.setStringProperty("appMessage", appMessage);
                msg.setStringProperty("_type", message.getClass().getName()); // Tipo dinámico del mensaje
                return msg;
            });
            LOGGER.info("Message sent from {}: {}", "APP = auth_api", message);

        } catch (Exception e) {
            System.err.println("⚠️ Error enviando mensaje: " + e.getMessage());
        }
    }
}
