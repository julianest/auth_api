package com.users.auth_api.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@AllArgsConstructor
public class JmsMessageService {

    private JmsTemplate jmsTemplate;

    public void sendEvent(String appMessage,String eventType, Object message) {
        String typeActiveMQ;
        if( "LOGOUT".equals(eventType)){
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
                msg.setStringProperty("_type", message.getClass().getName());
                return msg;
            });
            log.info("Message sent from {}: {}", "APP = auth_api", message);

        } catch (Exception e) {
            log.error("Error enviando mensaje: " + e.getMessage());
        }
    }
}
