package io.github.henriquemichelini.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.henriquemichelini.dto.EmailResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class HuggingFaceClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${huggingface.api.key:}")
    private String apiKey;

    @Value("${huggingface.api.url:https://router.huggingface.co/hf-inference/models/}")
    private String apiUrl;

    @Value("${huggingface.model:cardiffnlp/twitter-xlm-roberta-base-sentiment}")
    private String model;

    public EmailResponseDTO classifyWithHuggingFace(String emailText) {
        log.debug("Classificando com Hugging Face");

        if (apiKey == null || apiKey.isEmpty() || apiKey.equals("your-huggingface-token-here")) {
            log.warn("Hugging Face API Key não configurada. Usando fallback.");
            return fallbackClassification(emailText);
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            headers.setBearerAuth(apiKey);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("inputs", emailText);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            String url = apiUrl + model;
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            return processHuggingFaceResponse(response.getBody(), emailText);

        } catch (Exception e) {
            log.error("Erro ao chamar Hugging Face: {}", e.getMessage(), e);
            log.info("Usando fallback devido a erro");
            return fallbackClassification(emailText);
        }
    }

    private EmailResponseDTO processHuggingFaceResponse(String responseBody, String emailText) {
        try {
            JsonNode root = objectMapper.readTree(responseBody);

            if (root.isArray() && !root.isEmpty()) {
                JsonNode firstResult = root.get(0);

                if (firstResult.isArray() && !firstResult.isEmpty()) {
                    String topLabel = firstResult.get(0).path("label").asText();
                    double topScore = firstResult.get(0).path("score").asDouble();

                    log.info("Hugging Face classificou como: {} (score: {})", topLabel, topScore);

                    String categoria = mapLabelToCategory(topLabel, emailText);
                    String respostaSugerida = generateResponse(categoria);

                    return new EmailResponseDTO(categoria, respostaSugerida);
                }
            }

            log.warn("Formato de resposta inesperado do Hugging Face");
            return fallbackClassification(emailText);

        } catch (Exception e) {
            log.error("Erro ao processar resposta do Hugging Face: {}", e.getMessage());
            return fallbackClassification(emailText);
        }
    }

    private String mapLabelToCategory(String label, String emailText) {
        if (label.toLowerCase().contains("negative") || label.toLowerCase().contains("neg")) {
            return "Produtivo";
        } else if (label.toLowerCase().contains("positive") || label.toLowerCase().contains("pos")) {
            return "Improdutivo";
        }

        return fallbackClassification(emailText).categoria();
    }

    private String generateResponse(String categoria) {
        if (categoria.equals("Produtivo")) {
            return """
                    Prezado(a),
                    
                    Recebemos sua solicitação e ela está sendo analisada por nossa equipe. \
                    Em breve retornaremos com mais informações.
                    
                    Agradecemos o contato e ficamos à disposição.
                    
                    Atenciosamente,
                    Equipe de Atendimento""";
        } else {
            return """
                    Prezado(a),
                    
                    Agradecemos sua mensagem! \
                    Ficamos felizes com o seu contato.
                    
                    Atenciosamente,
                    Equipe de Atendimento""";
        }
    }

    private EmailResponseDTO fallbackClassification(String emailText) {
        log.info("Usando classificação fallback");

        String emailLower = emailText.toLowerCase();

        String[] produtiveKeywords = {
                "solicitação", "solicito", "solicitar", "preciso", "necessito",
                "urgente", "problema", "erro", "dúvida", "questão", "consulta",
                "atualização", "status", "andamento", "prazo", "pendência",
                "suporte", "ajuda", "auxílio", "informação", "documento",
                "financeiro", "pagamento", "fatura", "cobrança", "transação",
                "sistema", "acesso", "senha", "login", "conta", "cadastro"
        };

        String[] unproductiveKeywords = {
                "feliz natal", "feliz ano novo", "parabéns", "felicitações",
                "aniversário", "obrigado", "agradeço", "agradecimento",
                "bom dia", "boa tarde", "boa noite", "fim de semana",
                "feriado", "férias"
        };

        int produtiveScore = 0;
        int unproductiveScore = 0;

        for (String keyword : produtiveKeywords) {
            if (emailLower.contains(keyword)) {
                produtiveScore++;
            }
        }

        for (String keyword : unproductiveKeywords) {
            if (emailLower.contains(keyword)) {
                unproductiveScore++;
            }
        }

        boolean isProdutivo = produtiveScore > unproductiveScore ||
                (produtiveScore == 0 && unproductiveScore == 0 && emailText.length() > 100);

        String categoria = isProdutivo ? "Produtivo" : "Improdutivo";
        String respostaSugerida = generateResponse(categoria);

        return new EmailResponseDTO(categoria, respostaSugerida);
    }
}