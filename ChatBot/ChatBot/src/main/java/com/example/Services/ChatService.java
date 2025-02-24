package com.example.Services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.StreamingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.example.payload.CricketResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;

@Service
public class ChatService {
    @Autowired
    private ChatModel chatModel;
    @Autowired
    private StreamingModel streamingModel;


    public String generateResponse(String inputText) {
        //here using ai chat model to generate response
        String response = chatModel.call(inputText);
        return response;
        
    }

    public Flux<String> stringResponse(String inputText) {
        //here using ai chat model to generate response
        Flux<String> response = chatModel.stream(inputText);
        return response;
    }

    public CricketResponse generaCricketResponse(String inputText) throws JsonProcessingException {
        //here using ai chat model to generate response
       String promptString = "";

       ChatResponse cricketResponse = chatModel.call(
            new Prompt(promptString)
       ); 
        //get content as string
        String responseString =  cricketResponse.getResult().getOutput().getText();
        
        ObjectMapper mapper = new ObjectMapper();
        CricketResponse cricketResponse1 = mapper.readValue(responseString, CricketResponse.class);
        return cricketResponse1;
    }

    //load prompt from classpath
    public String loadPrompt(String filename) throws IOException{
        Path filePath = new ClassPathResource(filename).getFile().toPath();
        return Files.readString(filePath);
    }
}
