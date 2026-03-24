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

    // Severity levels
    private static final String SEVERITY_ERROR = "error";
    private static final String SEVERITY_CRITICAL = "critical";

    // Error messages and codes
    private static final String ERROR_CODE_USER_NOT_FOUND = "USER_NOT_FOUND";
    private static final String ERROR_CODE_RESOURCE_NOT_FOUND = "RESOURCE_NOT_FOUND";
    private static final String ERROR_CODE_DUPLICATE_EMAIL = "DUPLICATE_EMAIL";
    private static final String ERROR_CODE_INVALID_CREDENTIALS = "INVALID_CREDENTIALS";
    private static final String ERROR_CODE_VALIDATION_FAILED = "VALIDATION_FAILED";
    private static final String ERROR_CODE_INVALID_ARGUMENT = "INVALID_ARGUMENT";
    private static final String ERROR_CODE_INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";

    // Error titles
    private static final String TITLE_USER_NOT_FOUND = "Usuário Não Encontrado";
    private static final String TITLE_RESOURCE_NOT_FOUND = "Recurso Não Encontrado";
    private static final String TITLE_DUPLICATE_EMAIL = "Email Duplicado";
    private static final String TITLE_INVALID_CREDENTIALS = "Credenciais Inválidas";
    private static final String TITLE_VALIDATION_ERROR = "Erro de Validação";
    private static final String TITLE_INVALID_ARGUMENT = "Argumento Inválido";
    private static final String TITLE_INTERNAL_ERROR = "Erro Interno do Servidor";

    // Property names
    private static final String PROP_TRACE_ID = "traceId";
    private static final String PROP_ERROR_CODE = "errorCode";
    private static final String PROP_SUGGESTION = "suggestion";
    private static final String PROP_VALIDATION_ERRORS = "validationErrors";
    private static final String PROP_ERROR_COUNT = "errorCount";
    private static final String PROP_EXCEPTION_TYPE = "exceptionType";

    private ProblemDetail buildProblemDetail(HttpStatus status, String title, String detail, String severity, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        String traceId = UUID.randomUUID().toString();
        
        problemDetail.setTitle(title);
        problemDetail.setDetail(detail);
        problemDetail.setType(URI.create("https://api.fiap.com/errors/" + status.value()));
        problemDetail.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));
        
        problemDetail.setProperty(PROP_TRACE_ID, traceId);
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
            TITLE_USER_NOT_FOUND,
            ex.getMessage(),
            SEVERITY_ERROR,
            request
        );
        problemDetail.setProperty(PROP_ERROR_CODE, ERROR_CODE_USER_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        ProblemDetail problemDetail = buildProblemDetail(
            HttpStatus.NOT_FOUND,
            TITLE_RESOURCE_NOT_FOUND,
            ex.getMessage(),
            SEVERITY_ERROR,
            request
        );
        problemDetail.setProperty(PROP_ERROR_CODE, ERROR_CODE_RESOURCE_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ProblemDetail> handleDuplicateEmailException(
            DuplicateEmailException ex, WebRequest request) {
        ProblemDetail problemDetail = buildProblemDetail(
            HttpStatus.CONFLICT,
            TITLE_DUPLICATE_EMAIL,
            ex.getMessage(),
            SEVERITY_ERROR,
            request
        );
        problemDetail.setProperty(PROP_ERROR_CODE, ERROR_CODE_DUPLICATE_EMAIL);
        problemDetail.setProperty(PROP_SUGGESTION, "Use um email diferente ou recupere sua senha");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(problemDetail);
    }

    @ExceptionHandler(InvalidLoginException.class)
    public ResponseEntity<ProblemDetail> handleInvalidLoginException(
            InvalidLoginException ex, WebRequest request) {
        ProblemDetail problemDetail = buildProblemDetail(
            HttpStatus.UNAUTHORIZED,
            TITLE_INVALID_CREDENTIALS,
            ex.getMessage(),
            SEVERITY_ERROR,
            request
        );
        problemDetail.setProperty(PROP_ERROR_CODE, ERROR_CODE_INVALID_CREDENTIALS);
        problemDetail.setProperty(PROP_SUGGESTION, "Verifique seu login e senha");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problemDetail);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, WebRequest request) {
        ProblemDetail problemDetail = buildProblemDetail(
            HttpStatus.BAD_REQUEST,
            TITLE_VALIDATION_ERROR,
            "Um ou mais campos contêm dados inválidos",
            SEVERITY_ERROR,
            request
        );
        problemDetail.setProperty(PROP_ERROR_CODE, ERROR_CODE_VALIDATION_FAILED);

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });

        problemDetail.setProperty(PROP_VALIDATION_ERRORS, fieldErrors);
        problemDetail.setProperty(PROP_ERROR_COUNT, fieldErrors.size());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        ProblemDetail problemDetail = buildProblemDetail(
            HttpStatus.BAD_REQUEST,
            TITLE_INVALID_ARGUMENT,
            ex.getMessage(),
            SEVERITY_ERROR,
            request
        );
        problemDetail.setProperty(PROP_ERROR_CODE, ERROR_CODE_INVALID_ARGUMENT);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGeneralException(
            Exception ex, WebRequest request) {
        ProblemDetail problemDetail = buildProblemDetail(
            HttpStatus.INTERNAL_SERVER_ERROR,
            TITLE_INTERNAL_ERROR,
            "Ocorreu um erro inesperado. Use o traceId para investigação",
            SEVERITY_CRITICAL,
            request
        );
        problemDetail.setProperty(PROP_ERROR_CODE, ERROR_CODE_INTERNAL_SERVER_ERROR);
        problemDetail.setProperty(PROP_EXCEPTION_TYPE, ex.getClass().getSimpleName());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
    }
}
