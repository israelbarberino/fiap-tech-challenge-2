package com.fiap.challenge.infrastructure.persistence.usertype;

import com.fiap.challenge.domain.usertype.model.UserTypeAggregate;
import com.fiap.challenge.domain.usertype.port.UserTypeGateway;
import com.fiap.challenge.entity.UserType;
import com.fiap.challenge.repository.UserTypeRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserTypeJpaGateway implements UserTypeGateway {

    private final UserTypeRepository userTypeRepository;

    public UserTypeJpaGateway(UserTypeRepository userTypeRepository) {
        this.userTypeRepository = userTypeRepository;
    }

    @Override
    public UserTypeAggregate save(UserTypeAggregate userType) {
        UserType entity = toEntity(userType);
        UserType savedEntity = userTypeRepository.save(entity);
        return toAggregate(savedEntity);
    }

    @Override
    public Optional<UserTypeAggregate> findById(Long id) {
        return userTypeRepository.findById(id).map(this::toAggregate);
    }

    @Override
    public Optional<UserTypeAggregate> findByName(String name) {
        return userTypeRepository.findByName(name).map(this::toAggregate);
    }

    @Override
    public List<UserTypeAggregate> findAll() {
        return userTypeRepository.findAll().stream()
                .map(this::toAggregate)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        userTypeRepository.deleteById(id);
    }

    private UserTypeAggregate toAggregate(UserType entity) {
        return new UserTypeAggregate(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getCreatedAt(),
                entity.getLastModifiedAt()
        );
    }

    private UserType toEntity(UserTypeAggregate aggregate) {
        UserType entity = new UserType(aggregate.name(), aggregate.description());
        entity.setId(aggregate.id());
        entity.setCreatedAt(aggregate.createdAt());
        entity.setLastModifiedAt(aggregate.lastModifiedAt());
        return entity;
    }
}
