package com.fiap.challenge.application.usertype.usecase;

import com.fiap.challenge.application.usertype.dto.CreateUserTypeCommand;
import com.fiap.challenge.application.usertype.dto.UserTypeResult;
import com.fiap.challenge.application.usertype.mapper.UserTypeApplicationMapper;
import com.fiap.challenge.domain.usertype.model.UserTypeAggregate;
import com.fiap.challenge.domain.usertype.port.UserTypeGateway;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateUserTypeUseCase {

    private final UserTypeGateway userTypeGateway;

    public CreateUserTypeUseCase(UserTypeGateway userTypeGateway) {
        this.userTypeGateway = userTypeGateway;
    }

    public UserTypeResult execute(CreateUserTypeCommand command) {
        UserTypeAggregate aggregateToCreate = new UserTypeAggregate(
                null,
                command.name(),
                command.description(),
                null,
                null
        );

        UserTypeAggregate savedAggregate = userTypeGateway.save(aggregateToCreate);
        return UserTypeApplicationMapper.toResult(savedAggregate);
    }
}
