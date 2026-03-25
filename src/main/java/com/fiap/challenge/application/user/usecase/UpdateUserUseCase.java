package com.fiap.challenge.application.user.usecase;

import com.fiap.challenge.dto.UserResponse;
import com.fiap.challenge.dto.UserUpdateRequest;
import com.fiap.challenge.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UpdateUserUseCase {

    private final UserService userService;

    public UpdateUserUseCase(UserService userService) {
        this.userService = userService;
    }

    public UserResponse execute(Long id, UserUpdateRequest request) {
        return userService.updateUser(id, request);
    }
}
