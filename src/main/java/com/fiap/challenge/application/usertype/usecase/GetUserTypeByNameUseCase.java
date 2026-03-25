package com.fiap.challenge.application.usertype.usecase;

import com.fiap.challenge.application.usertype.dto.UserTypeResult;
import com.fiap.challenge.application.usertype.mapper.UserTypeApplicationMapper;
import com.fiap.challenge.domain.usertype.model.UserTypeAggregate;
import com.fiap.challenge.domain.usertype.port.UserTypeGateway;
import com.fiap.challenge.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GetUserTypeByNameUseCase {

    private static final String USER_TYPE_NOT_FOUND_NAME_MSG = "UserType not found with name: ";

    private final UserTypeGateway userTypeGateway;

    public GetUserTypeByNameUseCase(UserTypeGateway userTypeGateway) {
        this.userTypeGateway = userTypeGateway;
    }

    public UserTypeResult execute(String name) {
        UserTypeAggregate aggregate = userTypeGateway.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(USER_TYPE_NOT_FOUND_NAME_MSG + name));
        return UserTypeApplicationMapper.toResult(aggregate);
    }
}
