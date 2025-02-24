package com.example.ChatBot;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.Services.ChatService;
import com.example.payload.CricketResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

@SpringBootTest
class ChatBotApplicationTests {

	@Autowired
	private ChatService chatService;

	@Test
	void contextLoads() throws JsonProcessingException {
		CricketResponse cricketResponse = chatService.generaCricketResponse("Who is scahin tendulkar?");
		System.out.println(cricketResponse.getContent());
	}

	@Test
	void testTemplate() throws IOException {
		// Test the template
		String s = chatService.loadPrompt("prompts/cricket_bot.txt");
		System.out.println(s);
	}
}
