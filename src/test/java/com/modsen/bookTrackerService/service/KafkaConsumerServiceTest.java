package com.modsen.bookTrackerService.service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.bookTrackerService.model.BookStatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class KafkaConsumerServiceTest {

    @Mock
    private BookTrackerService bookTrackerService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private KafkaConsumerService kafkaConsumerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listen_createAction() throws Exception {
        String message = "{\"action\":\"create\",\"bookId\":\"1\"}";
        JsonNode jsonNode = mock(JsonNode.class);
        when(objectMapper.readTree(message)).thenReturn(jsonNode);
        when(jsonNode.get("action")).thenReturn(jsonNode);
        when(jsonNode.get("bookId")).thenReturn(jsonNode);
        when(jsonNode.asText()).thenReturn("create", "1");

        kafkaConsumerService.listen(message);

        verify(bookTrackerService, times(1)).createBookStatus("1");
        verify(bookTrackerService, never()).deleteBookStatus(anyString());
        verify(bookTrackerService, never()).updateBookStatus(anyString(), any(BookStatusEnum.class));
    }

    @Test
    void listen_deleteAction() throws Exception {
        String message = "{\"action\":\"delete\",\"bookId\":\"1\"}";
        JsonNode jsonNode = mock(JsonNode.class);
        when(objectMapper.readTree(message)).thenReturn(jsonNode);
        when(jsonNode.get("action")).thenReturn(jsonNode);
        when(jsonNode.get("bookId")).thenReturn(jsonNode);
        when(jsonNode.asText()).thenReturn("delete", "1");

        kafkaConsumerService.listen(message);

        verify(bookTrackerService, times(1)).deleteBookStatus("1");
        verify(bookTrackerService, never()).createBookStatus(anyString());
        verify(bookTrackerService, never()).updateBookStatus(anyString(), any(BookStatusEnum.class));
    }

    @Test
    void listen_takeAction() throws Exception {
        String message = "{\"action\":\"take\",\"bookId\":\"1\"}";
        JsonNode jsonNode = mock(JsonNode.class);
        when(objectMapper.readTree(message)).thenReturn(jsonNode);
        when(jsonNode.get("action")).thenReturn(jsonNode);
        when(jsonNode.get("bookId")).thenReturn(jsonNode);
        when(jsonNode.asText()).thenReturn("take", "1");

        kafkaConsumerService.listen(message);

        verify(bookTrackerService, times(1)).updateBookStatus("1", BookStatusEnum.CHECKED_OUT);
        verify(bookTrackerService, never()).createBookStatus(anyString());
        verify(bookTrackerService, never()).deleteBookStatus(anyString());
    }

    @Test
    void listen_returnAction() throws Exception {
        String message = "{\"action\":\"return\",\"bookId\":\"1\"}";
        JsonNode jsonNode = mock(JsonNode.class);
        when(objectMapper.readTree(message)).thenReturn(jsonNode);
        when(jsonNode.get("action")).thenReturn(jsonNode);
        when(jsonNode.get("bookId")).thenReturn(jsonNode);
        when(jsonNode.asText()).thenReturn("return", "1");

        kafkaConsumerService.listen(message);

        verify(bookTrackerService, times(1)).updateBookStatus("1", BookStatusEnum.AVAILABLE);
        verify(bookTrackerService, never()).createBookStatus(anyString());
        verify(bookTrackerService, never()).deleteBookStatus(anyString());
    }

    @Test
    void listen_invalidAction() throws Exception {
        String message = "{\"action\":\"invalid\",\"bookId\":\"1\"}";
        JsonNode jsonNode = mock(JsonNode.class);
        when(objectMapper.readTree(message)).thenReturn(jsonNode);
        when(jsonNode.get("action")).thenReturn(jsonNode);
        when(jsonNode.get("bookId")).thenReturn(jsonNode);
        when(jsonNode.asText()).thenReturn("invalid", "1");

        kafkaConsumerService.listen(message);

        verify(bookTrackerService, never()).createBookStatus(anyString());
        verify(bookTrackerService, never()).deleteBookStatus(anyString());
        verify(bookTrackerService, never()).updateBookStatus(anyString(), any(BookStatusEnum.class));
    }
}

