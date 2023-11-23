package com.cedup.super_preco.controller.produto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class ChatGPT {

    // limite de token = https://platform.openai.com/docs/models/gpt-3-5
    private static final String endpoint = "https://api.openai.com/v1/chat/completions";
    private static final String OPENAI_MODEL = "gpt-4-1106-preview";
    String systemMessage = "";
    String userMessage = "Estou enviando uma lista de produtos de diferentes mercados. " +
            "Gostaria que você analisasse todos os produtos levando em consideração a marca, seção, " +
            "sabor e o volume (em ml ou l). " +
            "Gostaria que você agrupe os produtos que são exatamente iguais (mesma marca, sabor e volume) " +
            "mas estão escritos de maneiras diferentes em diferentes mercados. " +
            "Para cada grupo de produtos correspondentes, gere um 'id_grupo'. " +
            "Para um produto que não tem correspondência, você deve atribuir um id_grupo único. " +
            "Por exemplo, se tivermos os produtos 'Pepsi 2L', 'pepsi 2l', 'Pepsi dois litros', " +
            "todos os três produtos pertencem ao mesmo grupo, porque são o mesmo produto, mas foram escritos de maneiras diferentes." +
            "Por favor, retorne a resposta no formato JSON puro. Aqui está um exemplo do formato que eu gostaria: " +
            "{ 'id_grupo': 'numero do id gerado aqui', 'id_produto': ['id do produto 1', 'id do produto 2', 'id do produto n'] }";

    @Value("${openai-api-key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getOpenAIResponse(String prompt) {
        // Crie o corpo da solicitação JSON
        String requestJson = "{"
                + "\"model\":\"" + OPENAI_MODEL + "\","
                + "\"messages\":["
                + "{\"role\":\"system\",\"content\":\"" + systemMessage + "\"},"
                + "{\"role\":\"user\",\"content\":\"" + userMessage + "\"},"
                + "{\"role\":\"user\",\"content\":\"" + prompt + "\"}"
                + "]"
                + "}";

        System.out.println(prompt);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(endpoint, entity, String.class);

        return response.getBody();
    }
}