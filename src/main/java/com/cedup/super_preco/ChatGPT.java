package com.cedup.super_preco;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;


@Service
public class ChatGPT {
    private static final String HTTPS_API_OPENAI_COM_V_1_COMPLETIONS = "https://api.openai.com/v1/completions";
    private static final String OPENAI_MODEL = "gpt-3.5-turbo-instruct";

    @Value("${openai-api-key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getOpenAIResponse(String prompt) {
        final String requestJson = "{\"prompt\":\"" + prompt + "\",\"model\":\"" + OPENAI_MODEL + "\",\"max_tokens\":4000}";

        // Imprima o JSON da solicitação
        System.out.println("JSON da solicitação: " + requestJson);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(HTTPS_API_OPENAI_COM_V_1_COMPLETIONS, entity, String.class);

        return response.getBody();
    }
}