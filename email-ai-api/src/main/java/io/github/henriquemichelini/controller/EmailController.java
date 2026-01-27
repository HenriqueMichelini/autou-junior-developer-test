package io.github.henriquemichelini.controller;

import io.github.henriquemichelini.dto.EmailRequestDTO;
import io.github.henriquemichelini.dto.EmailResponseDTO;
import io.github.henriquemichelini.service.EmailClassificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/v1/emails")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EmailController {

    private final EmailClassificationService classificationService;

    @PostMapping("/classify")
    public ResponseEntity<EmailResponseDTO> classifyEmail(
            @Valid @RequestBody EmailRequestDTO request) {

        log.info("Recebida requisição para classificar email via texto");

        EmailResponseDTO response = classificationService.classifyEmail(request.text());

        log.info("Email classificado como: {}", response.categoria());

        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/classify/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EmailResponseDTO> classifyEmailFromFile(
            @RequestParam("file") MultipartFile file) {

        log.info("Recebida requisição para classificar email via arquivo: {}",
                file.getOriginalFilename());

        EmailResponseDTO response = classificationService.classifyEmailFromFile(file);

        log.info("Email do arquivo classificado como: {}", response.categoria());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Email Classification API is running");
    }
}