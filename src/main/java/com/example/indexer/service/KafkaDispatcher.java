package com.example.indexer.service;

import com.example.indexer.model.MockAccount;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaDispatcher {

    @Value("${app.kafka.topic}")
    private String accountTopic;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void send(final MockAccount account) {
        try {
            final String messageKey = account.getId().toString();
            final String serializedAccount = serialize(account);
            this.kafkaTemplate.send(accountTopic, messageKey, serializedAccount);
            log.debug("Account has sent: {} to the topic: {}", serializedAccount, accountTopic);
        } catch (NullPointerException npe) {
            log.warn("Skipping account since missing account id. Account: {}", account);
        }
    }

    private String serialize(final MockAccount tweet) {
        try {
            return objectMapper.writeValueAsString(tweet);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error has occurred while serializing message.");
        }
    }

}
