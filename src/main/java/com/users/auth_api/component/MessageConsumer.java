package com.users.auth_api.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.users.auth_api.entity.SystemMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageConsumer.class);

    @JmsListener(destination = "auth-queue")
    public void messageListener(String messageJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            SystemMessage systemMessage = objectMapper.readValue(messageJson, SystemMessage.class);
            LOGGER.info("Message received: {}", systemMessage);
        } catch (Exception e) {
            LOGGER.error("Error processing message: {}", e.getMessage(), e);
        }
    }
}
