package com.fiap.challenge.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ProblemDetail;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.core.MethodParameter;

import java.lang.reflect.Method;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();
    private final WebRequest webRequest = new ServletWebRequest(new MockHttpServletRequest());

    @Test
    @DisplayName("Deve criar ProblemDetail para usuario nao encontrado")
    void shouldHandleUserNotFound() {
        ProblemDetail detail = handler.handleUserNotFoundException(
            new UserNotFoundException("Usuario nao encontrado"), webRequest).getBody();

        assertNotNull(detail);
        assertEquals("USER_NOT_FOUND", detail.getProperties().get("errorCode"));
        assertEquals(404, detail.getProperties().get("status"));
    }

    @Test
    @DisplayName("Deve criar ProblemDetail para email duplicado")
    void shouldHandleDuplicateEmail() {
        ProblemDetail detail = handler.handleDuplicateEmailException(
            new DuplicateEmailException("Email duplicado"), webRequest).getBody();

        assertNotNull(detail);
        assertEquals("DUPLICATE_EMAIL", detail.getProperties().get("errorCode"));
        assertEquals("Email duplicado", detail.getDetail());
    }

    @Test
    @DisplayName("Deve criar ProblemDetail para login invalido")
    void shouldHandleInvalidLogin() {
        ProblemDetail detail = handler.handleInvalidLoginException(
            new InvalidLoginException("Login invalido"), webRequest).getBody();

        assertNotNull(detail);
        assertEquals("INVALID_CREDENTIALS", detail.getProperties().get("errorCode"));
        assertEquals(401, detail.getProperties().get("status"));
    }

    @Test
    @DisplayName("Deve criar ProblemDetail para validacao de campos")
    void shouldHandleValidationErrors() throws NoSuchMethodException {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "target");
        bindingResult.addError(new FieldError("target", "email", "email invalido"));

        Method method = Dummy.class.getDeclaredMethod("dummy", String.class);
        MethodParameter methodParameter = new MethodParameter(method, 0);
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(methodParameter, bindingResult);

        ProblemDetail detail = handler.handleMethodArgumentNotValidException(ex, webRequest).getBody();

        assertNotNull(detail);
        assertEquals("VALIDATION_FAILED", detail.getProperties().get("errorCode"));
        Map<String, String> validationErrors = (Map<String, String>) detail.getProperties().get("validationErrors");
        assertEquals("email invalido", validationErrors.get("email"));
    }

    @Test
    @DisplayName("Deve criar ProblemDetail para argumento invalido")
    void shouldHandleIllegalArgument() {
        ProblemDetail detail = handler.handleIllegalArgumentException(
            new IllegalArgumentException("argumento invalido"), webRequest).getBody();

        assertNotNull(detail);
        assertEquals("INVALID_ARGUMENT", detail.getProperties().get("errorCode"));
    }

    @Test
    @DisplayName("Deve criar ProblemDetail para erro interno")
    void shouldHandleGeneralException() {
        ProblemDetail detail = handler.handleGeneralException(new RuntimeException("boom"), webRequest).getBody();

        assertNotNull(detail);
        assertEquals("INTERNAL_SERVER_ERROR", detail.getProperties().get("errorCode"));
        assertEquals("RuntimeException", detail.getProperties().get("exceptionType"));
    }

    private static class Dummy {
        @SuppressWarnings("unused")
        void dummy(String email) {
            // usado apenas para MethodParameter
        }
    }
}
