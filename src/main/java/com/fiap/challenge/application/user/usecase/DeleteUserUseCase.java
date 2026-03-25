package com.fiap.challenge.application.user.usecase;

import com.fiap.challenge.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class DeleteUserUseCase {

    private final UserService userService;

    public DeleteUserUseCase(UserService userService) {
        this.userService = userService;
    }

    public void execute(Long id) {
        userService.deleteUser(id);
    }
}
