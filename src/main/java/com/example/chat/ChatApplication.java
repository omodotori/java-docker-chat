package com.example.chat;

import com.example.chat.model.ChatMessage;
import com.example.chat.model.ChatMessage.MessageType;
import com.example.chat.repository.ChatMessageRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(ChatMessageRepository repository) {
		return args -> {
			ChatMessage msg = ChatMessage.builder()
				.type(MessageType.CHAT)
				.sender("system")
				.content("Hello from startup")
				.build();
			repository.save(msg);
			System.out.println("Saved test ChatMessage with id=" + msg.getId());
		};
	}

}
