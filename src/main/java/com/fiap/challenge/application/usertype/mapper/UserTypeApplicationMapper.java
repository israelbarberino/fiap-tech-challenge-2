package com.fiap.challenge.application.usertype.mapper;

import com.fiap.challenge.application.usertype.dto.UserTypeResult;
import com.fiap.challenge.domain.usertype.model.UserTypeAggregate;

public final class UserTypeApplicationMapper {

    private UserTypeApplicationMapper() {
    }

    public static UserTypeResult toResult(UserTypeAggregate aggregate) {
        return new UserTypeResult(
                aggregate.id(),
                aggregate.name(),
                aggregate.description(),
                aggregate.createdAt(),
                aggregate.lastModifiedAt()
        );
    }
}
