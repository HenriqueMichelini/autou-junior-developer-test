package io.github.henriquemichelini.util;

import io.github.henriquemichelini.exception.EmailProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class FileExtractor {
    public String extractText(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (filename == null) {
            throw new EmailProcessingException("Nome do arquivo não pode ser nulo");
        }

        String extension = getFileExtension(filename).toLowerCase();

        return switch (extension) {
            case "txt" -> extractTextFromTxt(file);
            case "pdf" -> extractTextFromPdf(file);
            default -> throw new EmailProcessingException(
                    "Tipo de arquivo não suportado: " + extension
            );
        };
    }

    private String extractTextFromTxt(MultipartFile file) {
        log.debug("Extraindo texto de arquivo TXT");

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            StringBuilder content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }

            String text = content.toString().trim();

            if (text.isEmpty()) {
                throw new EmailProcessingException("O arquivo está vazio");
            }

            return text;

        } catch (IOException e) {
            log.error("Erro ao ler arquivo TXT: {}", e.getMessage());
            throw new EmailProcessingException("Erro ao processar arquivo TXT", e);
        }
    }

    private String extractTextFromPdf(MultipartFile file) {
        log.debug("Extraindo texto de arquivo PDF");

        try (PDDocument document = PDDocument.load(file.getInputStream())) {

            if (document.isEncrypted()) {
                throw new EmailProcessingException(
                        "O arquivo PDF está criptografado. Por favor, envie um PDF sem proteção."
                );
            }

            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document).trim();

            if (text.isEmpty()) {
                throw new EmailProcessingException("O PDF não contém texto extraível");
            }

            return text;

        } catch (IOException e) {
            log.error("Erro ao ler arquivo PDF: {}", e.getMessage());
            throw new EmailProcessingException("Erro ao processar arquivo PDF", e);
        }
    }

    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }
        return filename.substring(lastDotIndex + 1);
    }
}