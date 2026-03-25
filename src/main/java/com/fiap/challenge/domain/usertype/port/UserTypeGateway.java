package com.fiap.challenge.domain.usertype.port;

import com.fiap.challenge.domain.usertype.model.UserTypeAggregate;

import java.util.List;
import java.util.Optional;

public interface UserTypeGateway {
    UserTypeAggregate save(UserTypeAggregate userType);

    Optional<UserTypeAggregate> findById(Long id);

    Optional<UserTypeAggregate> findByName(String name);

    List<UserTypeAggregate> findAll();

    void deleteById(Long id);
}
