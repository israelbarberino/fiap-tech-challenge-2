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
public class GetUserTypeByIdUseCase {

    private static final String USER_TYPE_NOT_FOUND_ID_MSG = "UserType not found with id: ";

    private final UserTypeGateway userTypeGateway;

    public GetUserTypeByIdUseCase(UserTypeGateway userTypeGateway) {
        this.userTypeGateway = userTypeGateway;
    }

    public UserTypeResult execute(Long id) {
        UserTypeAggregate aggregate = userTypeGateway.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(USER_TYPE_NOT_FOUND_ID_MSG + id));
        return UserTypeApplicationMapper.toResult(aggregate);
    }
}
