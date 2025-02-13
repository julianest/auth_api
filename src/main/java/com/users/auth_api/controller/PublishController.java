package com.users.auth_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.users.auth_api.entity.SystemMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublishController {

    @Autowired
    private JmsTemplate jmsTemplate;

    @PostMapping("/publishMessage")
    public ResponseEntity<String> publishMessage(@RequestBody SystemMessage systemMessage) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonMessage = objectMapper.writeValueAsString(systemMessage);
            System.out.println("Sending message as JSON: " + jsonMessage); // Log para verificar

            jmsTemplate.convertAndSend("auth-queue", jsonMessage);
            return ResponseEntity.ok("Sent to destination: auth-queue");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


}
