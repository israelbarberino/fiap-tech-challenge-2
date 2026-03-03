package com.fiap.challenge.service;

import com.fiap.challenge.dto.UserTypeRequestDTO;
import com.fiap.challenge.dto.UserTypeResponseDTO;
import com.fiap.challenge.entity.UserType;
import com.fiap.challenge.exception.ResourceNotFoundException;
import com.fiap.challenge.repository.UserTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço para gerenciar tipos de usuários.
 * Fornece operações CRUD para tipos de usuários.
 */
@Service
@Transactional
public class UserTypeService {

    private final UserTypeRepository userTypeRepository;

    public UserTypeService(UserTypeRepository userTypeRepository) {
        this.userTypeRepository = userTypeRepository;
    }

    /**
     * Cria um novo tipo de usuário.
     */
    public UserTypeResponseDTO create(UserTypeRequestDTO requestDTO) {
        UserType userType = new UserType(requestDTO.getName(), requestDTO.getDescription());
        UserType savedUserType = userTypeRepository.save(userType);
        return mapToResponseDTO(savedUserType);
    }

    /**
     * Busca um tipo de usuário por ID.
     */
    @Transactional(readOnly = true)
    public UserTypeResponseDTO getById(Long id) {
        UserType userType = userTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserType not found with id: " + id));
        return mapToResponseDTO(userType);
    }

    /**
     * Busca um tipo de usuário por nome.
     */
    @Transactional(readOnly = true)
    public UserTypeResponseDTO getByName(String name) {
        UserType userType = userTypeRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("UserType not found with name: " + name));
        return mapToResponseDTO(userType);
    }

    /**
     * Lista todos os tipos de usuários.
     */
    @Transactional(readOnly = true)
    public List<UserTypeResponseDTO> getAll() {
        return userTypeRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Atualiza um tipo de usuário existente.
     */
    public UserTypeResponseDTO update(Long id, UserTypeRequestDTO requestDTO) {
        UserType userType = userTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserType not found with id: " + id));
        
        userType.setName(requestDTO.getName());
        userType.setDescription(requestDTO.getDescription());
        
        UserType updatedUserType = userTypeRepository.save(userType);
        return mapToResponseDTO(updatedUserType);
    }

    /**
     * Deleta um tipo de usuário.
     */
    public void delete(Long id) {
        UserType userType = userTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserType not found with id: " + id));
        userTypeRepository.delete(userType);
    }

    private UserTypeResponseDTO mapToResponseDTO(UserType userType) {
        return new UserTypeResponseDTO(
                userType.getId(),
                userType.getName(),
                userType.getDescription(),
                userType.getCreatedAt(),
                userType.getLastModifiedAt()
        );
    }
}
