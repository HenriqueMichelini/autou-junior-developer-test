package io.github.henriquemichelini.service;

import io.github.henriquemichelini.client.HuggingFaceClient;
import io.github.henriquemichelini.dto.EmailResponseDTO;
import io.github.henriquemichelini.exception.EmailProcessingException;
import io.github.henriquemichelini.exception.UnsupportedFileTypeException;
import io.github.henriquemichelini.util.FileExtractor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailClassificationService {

    private final HuggingFaceClient aiClient;
    private final FileExtractor fileExtractor;

    public EmailResponseDTO classifyEmail(String emailText) {
        log.debug("Iniciando classificação de email");

        if (emailText == null || emailText.trim().isEmpty()) {
            throw new EmailProcessingException("O texto do email não pode estar vazio");
        }

        String processedText = preprocessText(emailText);

        EmailResponseDTO response = aiClient.classifyWithHuggingFace(processedText);

        log.debug("Classificação concluída: {}", response.categoria());

        return response;
    }

    public EmailResponseDTO classifyEmailFromFile(MultipartFile file) {
        log.debug("Processando arquivo: {}", file.getOriginalFilename());

        validateFile(file);

        String emailText = fileExtractor.extractText(file);

        return classifyEmail(emailText);
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new EmailProcessingException("Nenhum arquivo foi enviado");
        }

        String filename = file.getOriginalFilename();
        if (filename == null) {
            throw new EmailProcessingException("Nome do arquivo inválido");
        }

        String extension = getFileExtension(filename).toLowerCase();
        if (!extension.equals("txt") && !extension.equals("pdf")) {
            throw new UnsupportedFileTypeException(
                    "Tipo de arquivo não suportado: " + extension +
                            ". Por favor, envie um arquivo .txt ou .pdf"
            );
        }

        long maxSize = 5 * 1024 * 1024;
        if (file.getSize() > maxSize) {
            throw new EmailProcessingException(
                    "Arquivo muito grande. Tamanho máximo permitido: 5MB"
            );
        }
    }

    private String preprocessText(String text) {
        return text.trim()
                .replaceAll("\\s+", " ")
                .replaceAll("\\n{3,}", "\n\n");
    }

    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }
        return filename.substring(lastDotIndex + 1);
    }
}