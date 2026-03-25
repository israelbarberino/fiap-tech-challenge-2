package com.fiap.challenge.application.usertype.usecase;

import com.fiap.challenge.domain.usertype.port.UserTypeGateway;
import com.fiap.challenge.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeleteUserTypeUseCase {

    private static final String USER_TYPE_NOT_FOUND_ID_MSG = "UserType not found with id: ";

    private final UserTypeGateway userTypeGateway;

    public DeleteUserTypeUseCase(UserTypeGateway userTypeGateway) {
        this.userTypeGateway = userTypeGateway;
    }

    public void execute(Long id) {
        if (userTypeGateway.findById(id).isEmpty()) {
            throw new ResourceNotFoundException(USER_TYPE_NOT_FOUND_ID_MSG + id);
        }
        userTypeGateway.deleteById(id);
    }
}
