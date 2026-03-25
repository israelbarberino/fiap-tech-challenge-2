package com.fiap.challenge.application.user.usecase;

import com.fiap.challenge.dto.ChangePasswordRequest;
import com.fiap.challenge.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class ChangePasswordUseCase {

    private final UserService userService;

    public ChangePasswordUseCase(UserService userService) {
        this.userService = userService;
    }

    public void execute(Long id, ChangePasswordRequest request) {
        userService.changePassword(id, request);
    }
}
