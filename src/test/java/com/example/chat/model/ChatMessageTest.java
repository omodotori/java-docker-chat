package com.example.chat.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ChatMessage entity
 */
class ChatMessageTest {

    // ==================== Test Case 1 ====================
    @Test
    @DisplayName("TC-UT-01: ChatMessage builder should create valid message")
    void testBuilderCreatesValidMessage() {
        // Arrange & Act
        ChatMessage message = ChatMessage.builder()
                .type(ChatMessage.MessageType.CHAT)
                .sender("testUser")
                .content("Hello World!")
                .build();

        // Assert
        assertEquals("testUser", message.getSender());
        assertEquals("Hello World!", message.getContent());
        assertEquals(ChatMessage.MessageType.CHAT, message.getType());
    }

    // ==================== Test Case 2 ====================
    @Test
    @DisplayName("TC-UT-02: ChatMessage prePersist should set timestamp if null")
    void testPrePersistSetsTimestamp() {
        // Arrange
        ChatMessage message = new ChatMessage();
        assertNull(message.getTimestamp());

        // Act
        message.prePersist();

        // Assert
        assertNotNull(message.getTimestamp());
        assertTrue(message.getTimestamp().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    // ==================== Test Case 3 ====================
    @Test
    @DisplayName("TC-UT-03: ChatMessage prePersist should not override existing timestamp")
    void testPrePersistDoesNotOverrideExistingTimestamp() {
        // Arrange
        LocalDateTime existingTime = LocalDateTime.of(2025, 1, 1, 12, 0, 0);
        ChatMessage message = new ChatMessage();
        message.setTimestamp(existingTime);

        // Act
        message.prePersist();

        // Assert
        assertEquals(existingTime, message.getTimestamp());
    }

    // ==================== Test Case 4 ====================
    @Test
    @DisplayName("TC-UT-04: MessageType enum should contain CHAT, JOIN, LEAVE")
    void testMessageTypeEnumValues() {
        // Assert
        ChatMessage.MessageType[] types = ChatMessage.MessageType.values();
        assertEquals(3, types.length);
        assertNotNull(ChatMessage.MessageType.valueOf("CHAT"));
        assertNotNull(ChatMessage.MessageType.valueOf("JOIN"));
        assertNotNull(ChatMessage.MessageType.valueOf("LEAVE"));
    }

    // ==================== Test Case 5 ====================
    @Test
    @DisplayName("TC-UT-05: ChatMessage setters and getters should work correctly")
    void testSettersAndGetters() {
        // Arrange
        ChatMessage message = new ChatMessage();

        // Act
        message.setId(1L);
        message.setSender("user123");
        message.setContent("Test content");
        message.setType(ChatMessage.MessageType.JOIN);
        LocalDateTime now = LocalDateTime.now();
        message.setTimestamp(now);

        // Assert
        assertEquals(1L, message.getId());
        assertEquals("user123", message.getSender());
        assertEquals("Test content", message.getContent());
        assertEquals(ChatMessage.MessageType.JOIN, message.getType());
        assertEquals(now, message.getTimestamp());
    }

    // ==================== Test Case 6 ====================
    @Test
    @DisplayName("TC-UT-06: ChatMessage should allow null content")
    void testNullContentAllowed() {
        // Arrange & Act
        ChatMessage message = ChatMessage.builder()
                .type(ChatMessage.MessageType.JOIN)
                .sender("testUser")
                .content(null)
                .build();

        // Assert
        assertNull(message.getContent());
        assertEquals("testUser", message.getSender());
    }

    // ==================== Test Case 7 ====================
    @Test
    @DisplayName("TC-UT-07: ChatMessage should handle empty sender")
    void testEmptySender() {
        // Arrange & Act
        ChatMessage message = ChatMessage.builder()
                .type(ChatMessage.MessageType.CHAT)
                .sender("")
                .content("message")
                .build();

        // Assert
        assertEquals("", message.getSender());
    }

    // ==================== Test Case 8 ====================
    @Test
    @DisplayName("TC-UT-08: ChatMessage AllArgsConstructor should work")
    void testAllArgsConstructor() {
        // Arrange
        LocalDateTime time = LocalDateTime.now();

        // Act
        ChatMessage message = new ChatMessage(
                1L,
                ChatMessage.MessageType.LEAVE,
                "Goodbye",
                "leavingUser",
                time);

        // Assert
        assertEquals(1L, message.getId());
        assertEquals(ChatMessage.MessageType.LEAVE, message.getType());
        assertEquals("Goodbye", message.getContent());
        assertEquals("leavingUser", message.getSender());
        assertEquals(time, message.getTimestamp());
    }

    // ==================== Test Case 9 ====================
    @Test
    @DisplayName("TC-UT-09: ChatMessage NoArgsConstructor should create empty object")
    void testNoArgsConstructor() {
        // Act
        ChatMessage message = new ChatMessage();

        // Assert
        assertNull(message.getId());
        assertNull(message.getType());
        assertNull(message.getContent());
        assertNull(message.getSender());
        assertNull(message.getTimestamp());
    }

    // ==================== Test Case 10 ====================
    @Test
    @DisplayName("TC-UT-10: ChatMessage should handle long content")
    void testLongContent() {
        // Arrange
        String longContent = "A".repeat(10000);

        // Act
        ChatMessage message = ChatMessage.builder()
                .type(ChatMessage.MessageType.CHAT)
                .sender("user")
                .content(longContent)
                .build();

        // Assert
        assertEquals(10000, message.getContent().length());
        assertEquals(longContent, message.getContent());
    }
}
