package com.cedup.super_preco;

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
    private static final String HTTPS_API_OPENAI_COM_V_1_COMPLETIONS = "https://api.openai.com/v1/completions";
    private static final String OPENAI_MODEL = "gpt-4-1106-preview";

    @Value("${openai-api-key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getOpenAIResponse(String prompt) {
        // Crie o corpo da solicitação JSON
        String requestJson = "{"
                + "\"model\":\"" + OPENAI_MODEL + "\","
                + "\"messages\":["
                + "{\"role\":\"system\",\"content\":\"\"},"
                + "{\"role\":\"user\",\"content\":\"Analise todos os produtos a seguir. Levando em consideração a marca, a seção, o sabor e peso." +
                " Gere um id_grupo, atribua o mesmo id_grupo para produtos correspondentes entre os mercados e um id_grupo único para aquele produto que não teve nenhuma correspondência." +
                " Por favor, quero somente a resposta no formato json puro sem mais nenhum outro texto, exatamente dessa forma:" +
                " {id_grupo: insira o id(numero) gerado aqui, id_produto: [insira o/os id(s) dos produtos aqui]}}\"},"
                + "{\"role\":\"user\",\"content\":\"" + prompt + "\"}"
                + "]"
                + "}";

        System.out.println(prompt);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("https://api.openai.com/v1/chat/completions", entity, String.class);

        return response.getBody();
    }
}