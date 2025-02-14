package com.users.auth_api.component;

import jakarta.jms.TextMessage;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageConsumer.class);

    @JmsListener(destination = "auth-queue", containerFactory = "jmsListenerContainerFactory")
    public void messageListener(Object eventMessage) {
        try {
            String jsonMessage;

            // Extraer el contenido del mensaje si es de tipo ActiveMQTextMessage
            if (eventMessage instanceof ActiveMQTextMessage) {
                jsonMessage = ((ActiveMQTextMessage) eventMessage).getText();
            } else if (eventMessage instanceof TextMessage) {
                jsonMessage = ((TextMessage) eventMessage).getText();
            } else {
                LOGGER.warn("Tipo de mensaje desconocido: {}", eventMessage.getClass().getName());
                return;
            }
            System.out.println("📩 Evento recibido (JSON): " + jsonMessage);
            LOGGER.info("JSON Message received: {}", jsonMessage);

        } catch (Exception e) {
            LOGGER.error("Error processing message: {}", e.getMessage(), e);
        }
    }
}
