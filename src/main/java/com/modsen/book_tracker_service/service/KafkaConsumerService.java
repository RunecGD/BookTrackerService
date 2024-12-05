package com.modsen.book_tracker_service.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @Autowired
    private BookTrackerService bookTrackerService;
    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "book-status-topic", groupId = "book-tracker-group")
    public void listen(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            String action = jsonNode.get("action").asText();
            String bookId = jsonNode.get("bookId").asText();

            if ("create".equals(action)) {
                bookTrackerService.createBookStatus(bookId);
            } else if ("delete".equals(action)) {
                bookTrackerService.deleteBookStatus(bookId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
