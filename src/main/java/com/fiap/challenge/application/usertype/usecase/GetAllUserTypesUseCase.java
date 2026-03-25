package com.fiap.challenge.application.usertype.usecase;

import com.fiap.challenge.application.usertype.dto.UserTypeResult;
import com.fiap.challenge.application.usertype.mapper.UserTypeApplicationMapper;
import com.fiap.challenge.domain.usertype.port.UserTypeGateway;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class GetAllUserTypesUseCase {

    private final UserTypeGateway userTypeGateway;

    public GetAllUserTypesUseCase(UserTypeGateway userTypeGateway) {
        this.userTypeGateway = userTypeGateway;
    }

    public List<UserTypeResult> execute() {
        return userTypeGateway.findAll().stream()
                .map(UserTypeApplicationMapper::toResult)
                .toList();
    }
}
