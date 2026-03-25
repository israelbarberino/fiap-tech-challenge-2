package com.fiap.challenge.application.user.usecase;

import com.fiap.challenge.dto.UserResponse;
import com.fiap.challenge.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class GetUserByIdUseCase {

    private final UserService userService;

    public GetUserByIdUseCase(UserService userService) {
        this.userService = userService;
    }

    public UserResponse execute(Long id) {
        return userService.getUserById(id);
    }
}
