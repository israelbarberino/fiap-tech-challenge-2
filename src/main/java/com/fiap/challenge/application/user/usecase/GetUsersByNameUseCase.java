package com.fiap.challenge.application.user.usecase;

import com.fiap.challenge.dto.UserResponse;
import com.fiap.challenge.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetUsersByNameUseCase {

    private final UserService userService;

    public GetUsersByNameUseCase(UserService userService) {
        this.userService = userService;
    }

    public List<UserResponse> execute(String name) {
        return userService.getUsersByName(name);
    }
}
