package com.fiap.challenge.application.auth.usecase;

import com.fiap.challenge.dto.LoginValidateRequest;
import com.fiap.challenge.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class ValidateLoginUseCase {

    private final UserService userService;

    public ValidateLoginUseCase(UserService userService) {
        this.userService = userService;
    }

    public void execute(LoginValidateRequest request) {
        userService.validateLogin(request);
    }
}
