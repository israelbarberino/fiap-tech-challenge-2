package com.fiap.challenge.application.usertype.usecase;

import com.fiap.challenge.application.usertype.dto.UpdateUserTypeCommand;
import com.fiap.challenge.application.usertype.dto.UserTypeResult;
import com.fiap.challenge.application.usertype.mapper.UserTypeApplicationMapper;
import com.fiap.challenge.domain.usertype.model.UserTypeAggregate;
import com.fiap.challenge.domain.usertype.port.UserTypeGateway;
import com.fiap.challenge.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UpdateUserTypeUseCase {

    private static final String USER_TYPE_NOT_FOUND_ID_MSG = "UserType not found with id: ";

    private final UserTypeGateway userTypeGateway;

    public UpdateUserTypeUseCase(UserTypeGateway userTypeGateway) {
        this.userTypeGateway = userTypeGateway;
    }

    public UserTypeResult execute(Long id, UpdateUserTypeCommand command) {
        UserTypeAggregate currentAggregate = userTypeGateway.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(USER_TYPE_NOT_FOUND_ID_MSG + id));

        UserTypeAggregate aggregateToUpdate = new UserTypeAggregate(
                currentAggregate.id(),
                command.name(),
                command.description(),
                currentAggregate.createdAt(),
                currentAggregate.lastModifiedAt()
        );

        UserTypeAggregate updatedAggregate = userTypeGateway.save(aggregateToUpdate);
        return UserTypeApplicationMapper.toResult(updatedAggregate);
    }
}
