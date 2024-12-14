package com.modsen.bookTrackerService.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class KafkaConsumerService {

    private final BookTrackerService bookTrackerService;
    private final ObjectMapper objectMapper;

    public KafkaConsumerService(BookTrackerService bookTrackerService, ObjectMapper objectMapper) {
        this.bookTrackerService = bookTrackerService;
        this.objectMapper = objectMapper;
    }

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
            else if("take".equals(action)){
                bookTrackerService.updateBookStatus(bookId, "CHECKED_OUT");
            }
            else if("return".equals(action)){
                bookTrackerService.updateBookStatus(bookId, "AVAILABLE");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
