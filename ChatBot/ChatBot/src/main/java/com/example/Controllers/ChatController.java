package com.example.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Services.ChatService;
import com.example.payload.CricketResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import reactor.core.publisher.Flux;

@Controller
@RequestMapping("/api/v1/chat")
public class ChatController {
    @Autowired
    private ChatService chatService;

    @GetMapping
    public ResponseEntity<String> generateResponses(
            @RequestParam(value = "inputText", required = true) String inputText) {

        String responseText = chatService.generateResponse(inputText);
        return ResponseEntity.ok(responseText);

    }

    @GetMapping("/stream")
    public Flux<String> streamResponses(
            @RequestParam(value = "inputText", required = true) String inputText) {

        Flux<String> response = chatService.stringResponse(inputText);
        return response;
    }

    @GetMapping("/cricketbot")
    public ResponseEntity<CricketResponse> getCricketResponse(
            @RequestParam(value = "inputText", required = true) String inputText) throws JsonProcessingException {

        CricketResponse cricketResponse = chatService.generaCricketResponse(inputText);
        return ResponseEntity.ok(cricketResponse);
    }
}
