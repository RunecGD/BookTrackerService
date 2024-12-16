package com.modsen.bookTrackerService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.bookTrackerService.service.BookTrackerService;
import com.modsen.bookTrackerService.service.KafkaConsumerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class KafkaConsumerServiceTest {

    @Mock
    private BookTrackerService bookTrackerService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private KafkaConsumerService kafkaConsumerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testListen_CreateAction() throws Exception {
        
        String message = "{\"action\":\"create\", \"bookId\":\"1234\"}";
        JsonNode jsonNode = mock(JsonNode.class);
        when(objectMapper.readTree(message)).thenReturn(jsonNode);
        when(jsonNode.get("action")).thenReturn(mock(JsonNode.class));
        when(jsonNode.get("bookId")).thenReturn(mock(JsonNode.class));
        when(jsonNode.get("action").asText()).thenReturn("create");
        when(jsonNode.get("bookId").asText()).thenReturn("1234");

        
        kafkaConsumerService.listen(message);

        
        verify(bookTrackerService).createBookStatus("1234");
    }

    @Test
    public void testListen_DeleteAction() throws Exception {
        
        String message = "{\"action\":\"delete\", \"bookId\":\"1234\"}";
        JsonNode jsonNode = mock(JsonNode.class);
        when(objectMapper.readTree(message)).thenReturn(jsonNode);

        
        when(jsonNode.get("action")).thenReturn(mock(JsonNode.class));
        when(jsonNode.get("action").asText()).thenReturn("delete");
        when(jsonNode.get("bookId")).thenReturn(mock(JsonNode.class));
        when(jsonNode.get("bookId").asText()).thenReturn("1234");

        
        kafkaConsumerService.listen(message);

        
        verify(bookTrackerService).deleteBookStatus("1234");
    }

    @Test
    public void testListen_TakeAction() throws Exception {
        
        String message = "{\"action\":\"take\", \"bookId\":\"1234\"}";
        JsonNode jsonNode = mock(JsonNode.class);
        when(objectMapper.readTree(message)).thenReturn(jsonNode);

        
        when(jsonNode.get("action")).thenReturn(mock(JsonNode.class));
        when(jsonNode.get("action").asText()).thenReturn("take");
        when(jsonNode.get("bookId")).thenReturn(mock(JsonNode.class));
        when(jsonNode.get("bookId").asText()).thenReturn("1234");

        
        kafkaConsumerService.listen(message);

        
        verify(bookTrackerService).updateBookStatus("1234", "CHECKED_OUT");
    }

    @Test
    public void testListen_ReturnAction() throws Exception {
        
        String message = "{\"action\":\"return\", \"bookId\":\"1234\"}";
        JsonNode jsonNode = mock(JsonNode.class);
        when(objectMapper.readTree(message)).thenReturn(jsonNode);
        when(jsonNode.get("action")).thenReturn(mock(JsonNode.class));
        when(jsonNode.get("action").asText()).thenReturn("return");
        when(jsonNode.get("bookId")).thenReturn(mock(JsonNode.class));
        when(jsonNode.get("bookId").asText()).thenReturn("1234");

        
        kafkaConsumerService.listen(message);

        
        verify(bookTrackerService).updateBookStatus("1234", "AVAILABLE");
    }

    @Test
    public void testListen_InvalidAction() throws Exception {
        
        String message = "{\"action\":\"invalid\", \"bookId\":\"1234\"}";
        JsonNode jsonNode = mock(JsonNode.class);
        when(objectMapper.readTree(message)).thenReturn(jsonNode);

        
        when(jsonNode.get("action")).thenReturn(mock(JsonNode.class));
        when(jsonNode.get("action").asText()).thenReturn("invalid");
        when(jsonNode.get("bookId")).thenReturn(mock(JsonNode.class));
        when(jsonNode.get("bookId").asText()).thenReturn("1234");

        
        kafkaConsumerService.listen(message);

        
        verify(bookTrackerService, never()).createBookStatus(anyString());
        verify(bookTrackerService, never()).deleteBookStatus(anyString());
        verify(bookTrackerService, never()).updateBookStatus(anyString(), anyString());
    }

    @Test
    public void testListen_ExceptionHandling() throws Exception {
        
        String message = "{\"action\":\"create\", \"bookId\":\"1234\"}";
        when(objectMapper.readTree(message)).thenThrow(new RuntimeException("Test exception"));

        
        kafkaConsumerService.listen(message);

        
        verify(bookTrackerService, never()).createBookStatus(anyString());
    }
}
