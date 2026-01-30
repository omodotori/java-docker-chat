package com.example.chat.controller;

import com.example.chat.model.ChatMessage;
import com.example.chat.repository.ChatMessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ChatController
 */
@ExtendWith(MockitoExtension.class)
class ChatControllerTest {

    @Mock
    private ChatMessageRepository repository;

    @Mock
    private SimpMessageHeaderAccessor headerAccessor;

    @InjectMocks
    private ChatController chatController;

    private Map<String, Object> sessionAttributes;

    @BeforeEach
    void setUp() {
        sessionAttributes = new HashMap<>();
    }

    // ==================== Test Case 1 ====================
    @Test
    @DisplayName("TC-UT-01: sendMessage should save message and return it")
    void testSendMessageSavesAndReturns() {
        // Arrange
        ChatMessage message = ChatMessage.builder()
                .type(ChatMessage.MessageType.CHAT)
                .sender("testUser")
                .content("Hello!")
                .build();
        when(repository.save(any(ChatMessage.class))).thenReturn(message);

        // Act
        ChatMessage result = chatController.sendMessage(message);

        // Assert
        verify(repository, times(1)).save(message);
        assertEquals(message, result);
        assertEquals("testUser", result.getSender());
        assertEquals("Hello!", result.getContent());
    }

    // ==================== Test Case 2 ====================
    @Test
    @DisplayName("TC-UT-02: addUser should store username in session attributes")
    void testAddUserStoresUsername() {
        // Arrange
        ChatMessage message = ChatMessage.builder()
                .type(ChatMessage.MessageType.JOIN)
                .sender("newUser")
                .build();
        when(headerAccessor.getSessionAttributes()).thenReturn(sessionAttributes);
        when(repository.save(any(ChatMessage.class))).thenReturn(message);

        // Act
        ChatMessage result = chatController.addUser(message, headerAccessor);

        // Assert
        assertEquals("newUser", sessionAttributes.get("username"));
        verify(repository, times(1)).save(message);
        assertEquals(message, result);
    }

    // ==================== Test Case 3 ====================
    @Test
    @DisplayName("TC-UT-03: sendMessage should handle null content")
    void testSendMessageWithNullContent() {
        // Arrange
        ChatMessage message = ChatMessage.builder()
                .type(ChatMessage.MessageType.CHAT)
                .sender("user")
                .content(null)
                .build();
        when(repository.save(any(ChatMessage.class))).thenReturn(message);

        // Act
        ChatMessage result = chatController.sendMessage(message);

        // Assert
        assertNull(result.getContent());
        verify(repository).save(message);
    }

    // ==================== Test Case 4 ====================
    @Test
    @DisplayName("TC-UT-04: addUser should save JOIN message to database")
    void testAddUserSavesMessage() {
        // Arrange
        ChatMessage message = ChatMessage.builder()
                .type(ChatMessage.MessageType.JOIN)
                .sender("joiningUser")
                .build();
        when(headerAccessor.getSessionAttributes()).thenReturn(sessionAttributes);
        when(repository.save(any(ChatMessage.class))).thenReturn(message);

        // Act
        chatController.addUser(message, headerAccessor);

        // Assert
        verify(repository, times(1)).save(message);
    }

    // ==================== Test Case 5 ====================
    @Test
    @DisplayName("TC-UT-05: sendMessage should return same message instance")
    void testSendMessageReturnsSameInstance() {
        // Arrange
        ChatMessage message = ChatMessage.builder()
                .type(ChatMessage.MessageType.CHAT)
                .sender("user")
                .content("test")
                .build();
        when(repository.save(any(ChatMessage.class))).thenReturn(message);

        // Act
        ChatMessage result = chatController.sendMessage(message);

        // Assert
        assertSame(message, result);
    }

    // ==================== Test Case 6 ====================
    @Test
    @DisplayName("TC-UT-06: addUser should return message for broadcast")
    void testAddUserReturnsMEssageForBroadcast() {
        // Arrange
        ChatMessage message = ChatMessage.builder()
                .type(ChatMessage.MessageType.JOIN)
                .sender("broadcastUser")
                .build();
        when(headerAccessor.getSessionAttributes()).thenReturn(sessionAttributes);
        when(repository.save(any(ChatMessage.class))).thenReturn(message);

        // Act
        ChatMessage result = chatController.addUser(message, headerAccessor);

        // Assert
        assertNotNull(result);
        assertEquals(ChatMessage.MessageType.JOIN, result.getType());
    }

    // ==================== Test Case 7 ====================
    @Test
    @DisplayName("TC-UT-07: sendMessage should handle empty sender")
    void testSendMessageWithEmptySender() {
        // Arrange
        ChatMessage message = ChatMessage.builder()
                .type(ChatMessage.MessageType.CHAT)
                .sender("")
                .content("anonymous message")
                .build();
        when(repository.save(any(ChatMessage.class))).thenReturn(message);

        // Act
        ChatMessage result = chatController.sendMessage(message);

        // Assert
        assertEquals("", result.getSender());
    }

    // ==================== Test Case 8 ====================
    @Test
    @DisplayName("TC-UT-08: Multiple sendMessage calls should save each message")
    void testMultipleSendMessageCalls() {
        // Arrange
        ChatMessage msg1 = ChatMessage.builder().sender("user1").content("msg1").build();
        ChatMessage msg2 = ChatMessage.builder().sender("user2").content("msg2").build();
        when(repository.save(any(ChatMessage.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        chatController.sendMessage(msg1);
        chatController.sendMessage(msg2);

        // Assert
        verify(repository, times(2)).save(any(ChatMessage.class));
    }

    // ==================== Test Case 9 ====================
    @Test
    @DisplayName("TC-UT-09: addUser should preserve message type")
    void testAddUserPreservesMessageType() {
        // Arrange
        ChatMessage message = ChatMessage.builder()
                .type(ChatMessage.MessageType.JOIN)
                .sender("typeUser")
                .build();
        when(headerAccessor.getSessionAttributes()).thenReturn(sessionAttributes);
        when(repository.save(any(ChatMessage.class))).thenReturn(message);

        // Act
        ChatMessage result = chatController.addUser(message, headerAccessor);

        // Assert
        assertEquals(ChatMessage.MessageType.JOIN, result.getType());
    }

    // ==================== Test Case 10 ====================
    @Test
    @DisplayName("TC-UT-10: sendMessage with long content should work")
    void testSendMessageWithLongContent() {
        // Arrange
        String longContent = "A".repeat(5000);
        ChatMessage message = ChatMessage.builder()
                .type(ChatMessage.MessageType.CHAT)
                .sender("user")
                .content(longContent)
                .build();
        when(repository.save(any(ChatMessage.class))).thenReturn(message);

        // Act
        ChatMessage result = chatController.sendMessage(message);

        // Assert
        assertEquals(5000, result.getContent().length());
    }
}
