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
    // limite de token = https://platform.openai.com/docs/models/gpt-3-5
    private static final String HTTPS_API_OPENAI_COM_V_1_COMPLETIONS = "https://api.openai.com/v1/completions";
    private static final String OPENAI_MODEL = "gpt-3.5-turbo-16k";

    @Value("${openai-api-key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getOpenAIResponse(String prompt) {
        // Crie o corpo da solicitação JSON
        String requestJson = "{"
                + "\"model\":\"" + OPENAI_MODEL + "\","
                + "\"messages\":["
                + "{\"role\":\"system\",\"content\":\"Para cada mercado listado, analise os produtos a seguir. Levando em consideração a marca, a seção, o sabor e a massa, atribua o mesmo id_grupo para um produto do Mercado 1 e seu correspondente no Mercado 2. Se não encontrar um produto correspondente em algum mercado, gere um id_grupo único para aquele produto. Por favor, faça isso para todos os produtos listados.\"},"
                + "{\"role\":\"user\",\"content\":\"" + prompt + "\"}"
                + "]"
                + "}";

        // Imprima o JSON da solicitação
        System.out.println("JSON da solicitação: " + requestJson);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("https://api.openai.com/v1/chat/completions", entity, String.class);

        return response.getBody();
    }

}