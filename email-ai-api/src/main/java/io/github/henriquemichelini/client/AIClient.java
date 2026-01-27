package io.github.henriquemichelini.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.henriquemichelini.dto.EmailResponseDTO;
import io.github.henriquemichelini.exception.AIServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cliente para integração com API de IA (OpenAI GPT)
 *
 * Este cliente usa a API da OpenAI para:
 * 1. Classificar emails em "Produtivo" ou "Improdutivo"
 * 2. Gerar respostas automáticas apropriadas
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AIClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${openai.api.key:}")
    private String apiKey;

    @Value("${openai.api.url:https://api.openai.com/v1/chat/completions}")
    private String apiUrl;

    @Value("${openai.model:gpt-3.5-turbo}")
    private String model;

    private static final String SYSTEM_PROMPT = """
            Você é um assistente especializado em classificação de emails corporativos para uma empresa do setor financeiro.
            
            Sua tarefa é analisar emails e fazer duas coisas:
            1. Classificar o email em uma destas categorias:
               - "Produtivo": Emails que requerem ação ou resposta específica (solicitações de suporte, atualização de casos, dúvidas sobre sistemas, questões financeiras, etc.)
               - "Improdutivo": Emails que não necessitam ação imediata (felicitações, agradecimentos, mensagens sociais, etc.)
            
            2. Sugerir uma resposta automática apropriada para a categoria identificada.
            
            Responda SEMPRE no seguinte formato JSON (sem markdown, apenas o JSON puro):
            {
              "categoria": "Produtivo" ou "Improdutivo",
              "respostaSugerida": "texto da resposta sugerida"
            }
            
            A resposta sugerida deve ser:
            - Para emails Produtivos: Uma resposta profissional indicando que a solicitação foi recebida e está sendo processada
            - Para emails Improdutivos: Uma resposta educada e cordial
            
            Seja sempre profissional, objetivo e mantenha o tom corporativo adequado ao setor financeiro.
            """;


    public EmailResponseDTO classifyAndGenerateResponse(String emailText) {
        log.debug("Enviando email para classificação via IA");

        if (apiKey == null || apiKey.isEmpty() || apiKey.equals("your-api-key-here")) {
            log.warn("API Key não configurada. Usando classificação fallback.");
            return fallbackClassification(emailText);
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);
            requestBody.put("messages", List.of(
                    Map.of("role", "system", "content", SYSTEM_PROMPT),
                    Map.of("role", "user", "content", "Classifique este email:\n\n" + emailText)
            ));
            requestBody.put("temperature", 0.7);
            requestBody.put("max_tokens", 500);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            return parseAIResponse(response.getBody());

        } catch (Exception e) {
            log.error("Erro ao chamar API de IA: {}", e.getMessage(), e);
            return fallbackClassification(emailText);
        }
    }

    private EmailResponseDTO parseAIResponse(String responseBody) {
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            String content = root.path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

            content = content.replaceAll("```json\\s*", "").replaceAll("```\\s*", "").trim();

            JsonNode resultJson = objectMapper.readTree(content);

            String categoria = resultJson.path("categoria").asText();
            String respostaSugerida = resultJson.path("respostaSugerida").asText();

            return new EmailResponseDTO(categoria, respostaSugerida);

        } catch (Exception e) {
            log.error("Erro ao processar resposta da IA: {}", e.getMessage());
            throw new AIServiceException("Erro ao processar resposta da IA", e);
        }
    }

    private EmailResponseDTO fallbackClassification(String emailText) {
        log.info("Usando classificação fallback baseada em regras");

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
        String respostaSugerida;

        if (isProdutivo) {
            respostaSugerida = """
                    Prezado(a),
                    
                    Recebemos sua solicitação e ela está sendo analisada por nossa equipe. \
                    Em breve retornaremos com mais informações.
                    
                    Agradecemos o contato e ficamos à disposição.
                    
                    Atenciosamente,
                    Equipe de Atendimento""";
        } else {
            respostaSugerida = """
                    Prezado(a),
                    
                    Agradecemos sua mensagem! \
                    Ficamos felizes com o seu contato.
                    
                    Atenciosamente,
                    Equipe de Atendimento""";
        }

        return new EmailResponseDTO(categoria, respostaSugerida);
    }
}