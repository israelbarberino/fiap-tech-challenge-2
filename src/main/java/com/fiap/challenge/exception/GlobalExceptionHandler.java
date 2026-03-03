package com.fiap.challenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String SEVERITY_ERROR = "error";
    private static final String SEVERITY_CRITICAL = "critical";
    private ProblemDetail buildProblemDetail(HttpStatus status, String title, String detail, String severity, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        String traceId = UUID.randomUUID().toString();
        
        problemDetail.setTitle(title);
        problemDetail.setDetail(detail);
        problemDetail.setType(URI.create("https://api.fiap.com/errors/" + status.value()));
        problemDetail.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));
        
        problemDetail.setProperty("traceId", traceId);
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("severity", severity);
        problemDetail.setProperty("status", status.value());
        problemDetail.setProperty("error", status.getReasonPhrase());
        
        return problemDetail;
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleUserNotFoundException(
            UserNotFoundException ex, WebRequest request) {
        ProblemDetail problemDetail = buildProblemDetail(
            HttpStatus.NOT_FOUND,
            "Usuário Não Encontrado",
            ex.getMessage(),
            SEVERITY_ERROR,
            request
        );
        problemDetail.setProperty("errorCode", "USER_NOT_FOUND");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        ProblemDetail problemDetail = buildProblemDetail(
            HttpStatus.NOT_FOUND,
            "Recurso Não Encontrado",
            ex.getMessage(),
            SEVERITY_ERROR,
            request
        );
        problemDetail.setProperty("errorCode", "RESOURCE_NOT_FOUND");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ProblemDetail> handleDuplicateEmailException(
            DuplicateEmailException ex, WebRequest request) {
        ProblemDetail problemDetail = buildProblemDetail(
            HttpStatus.CONFLICT,
            "Email Duplicado",
            ex.getMessage(),
            SEVERITY_ERROR,
            request
        );
        problemDetail.setProperty("errorCode", "DUPLICATE_EMAIL");
        problemDetail.setProperty("suggestion", "Use um email diferente ou recupere sua senha");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(problemDetail);
    }

    @ExceptionHandler(InvalidLoginException.class)
    public ResponseEntity<ProblemDetail> handleInvalidLoginException(
            InvalidLoginException ex, WebRequest request) {
        ProblemDetail problemDetail = buildProblemDetail(
            HttpStatus.UNAUTHORIZED,
            "Credenciais Inválidas",
            ex.getMessage(),
            SEVERITY_ERROR,
            request
        );
        problemDetail.setProperty("errorCode", "INVALID_CREDENTIALS");
        problemDetail.setProperty("suggestion", "Verifique seu login e senha");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problemDetail);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, WebRequest request) {
        ProblemDetail problemDetail = buildProblemDetail(
            HttpStatus.BAD_REQUEST,
            "Erro de Validação",
            "Um ou mais campos contêm dados inválidos",
            SEVERITY_ERROR,
            request
        );
        problemDetail.setProperty("errorCode", "VALIDATION_FAILED");

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });

        problemDetail.setProperty("validationErrors", fieldErrors);
        problemDetail.setProperty("errorCount", fieldErrors.size());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        ProblemDetail problemDetail = buildProblemDetail(
            HttpStatus.BAD_REQUEST,
            "Argumento Inválido",
            ex.getMessage(),
            SEVERITY_ERROR,
            request
        );
        problemDetail.setProperty("errorCode", "INVALID_ARGUMENT");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGeneralException(
            Exception ex, WebRequest request) {
        ProblemDetail problemDetail = buildProblemDetail(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Erro Interno do Servidor",
            "Ocorreu um erro inesperado. Use o traceId para investigação",
            SEVERITY_CRITICAL,
            request
        );
        problemDetail.setProperty("errorCode", "INTERNAL_SERVER_ERROR");
        problemDetail.setProperty("exceptionType", ex.getClass().getSimpleName());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
    }
}
