package com.fiap.challenge.application.user.usecase;

import com.fiap.challenge.dto.UserCreateRequest;
import com.fiap.challenge.dto.UserResponse;
import com.fiap.challenge.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class CreateUserUseCase {

    private final UserService userService;

    public CreateUserUseCase(UserService userService) {
        this.userService = userService;
    }

    public UserResponse execute(UserCreateRequest request) {
        return userService.createUser(request);
    }
}
