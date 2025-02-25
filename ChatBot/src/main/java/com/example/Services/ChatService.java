package com.example.Services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.image.Image;
import org.springframework.ai.image.ImageGeneration;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImageOptionsBuilder;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.model.StreamingModel;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.example.payload.CricketResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;

@Service
public class ChatService {
    @Autowired
    private ChatModel chatModel;

    @Autowired
    private ImageModel imageModel;

    @Autowired
    private StreamingModel streamingModel;

    public String generateResponse(String inputText) {
        // here using ai chat model to generate response
        String response = chatModel.call(inputText);
        return response;

    }

    public Flux<String> stringResponse(String inputText) {
        // here using ai chat model to generate response
        Flux<String> response = chatModel.stream(inputText);
        return response;
    }

    public CricketResponse generaCricketResponse(String inputText) throws JsonProcessingException {
        // here using ai chat model to generate response
        String promptString = "";

        ChatResponse cricketResponse = chatModel.call(
                new Prompt(promptString));
        // get content as string
        String responseString = cricketResponse.getResult().getOutput().getText();

        ObjectMapper mapper = new ObjectMapper();
        CricketResponse cricketResponse1 = mapper.readValue(responseString, CricketResponse.class);
        return cricketResponse1;
    }

    // load prompt from classpath
    public List<String> generateImages(String imageDesc, String size, int numbers) throws IOException {
        // here using ai chat model to generate response
        String template = this.loadPrompt("prompts/image_bot.txt");
        String promptString = this.putValuesPromptTemplate(template, Map.of(
                "description", imageDesc
                ));

        ImageResponse imageResponse = imageModel.call(new ImagePrompt(
                promptString, OpenAiImageOptions.builder()
                        .withModel("dall-e-2")
                        .withN(numbers)
                        .withHeight(1024)
                        .withWidth(1024)
                        .withQuality("standard")
                        .build())
                        );
        List<String> imageUrl = imageResponse.getResults().stream().map(generation -> generation.getOutput().getUrl())
                .collect(Collectors.toList());
        return imageUrl;

    }

    public String loadPrompt(String filename) throws IOException {
        Path filePath = new ClassPathResource(filename).getFile().toPath();
        return Files.readString(filePath);
    }

    // put the values
    public String putValuesPromptTemplate(String template, Map<String, String> values) {
        for (Map.Entry<String, String> entry : values.entrySet()) {
            template = template.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return template;
    }
}
