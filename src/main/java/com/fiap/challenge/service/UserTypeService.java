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
 * Fornece operações CRUD com validação e mapeamento de DTOs.
 */
@Service
@Transactional
public class UserTypeService {

    private static final String USER_TYPE_NOT_FOUND_ID_MSG = "UserType not found with id: ";
    private static final String USER_TYPE_NOT_FOUND_NAME_MSG = "UserType not found with name: ";

    private final UserTypeRepository userTypeRepository;

    public UserTypeService(UserTypeRepository userTypeRepository) {
        this.userTypeRepository = userTypeRepository;
    }

    /**
     * Cria um novo tipo de usuário.
     *
     * @param requestDTO dados do tipo de usuário a ser criado
     * @return DTO com dados do tipo criado
     */
    public UserTypeResponseDTO create(UserTypeRequestDTO requestDTO) {
        UserType userType = new UserType(requestDTO.getName(), requestDTO.getDescription());
        UserType savedUserType = userTypeRepository.save(userType);
        return mapToResponseDTO(savedUserType);
    }

    /**
     * Busca um tipo de usuário por ID.
     *
     * @param id ID do tipo de usuário
     * @return DTO com dados do tipo
     * @throws ResourceNotFoundException se não encontrado
     */
    @Transactional(readOnly = true)
    public UserTypeResponseDTO getById(Long id) {
        UserType userType = findUserTypeById(id);
        return mapToResponseDTO(userType);
    }

    /**
     * Busca um tipo de usuário por nome.
     *
     * @param name nome do tipo de usuário
     * @return DTO com dados do tipo
     * @throws ResourceNotFoundException se não encontrado
     */
    @Transactional(readOnly = true)
    public UserTypeResponseDTO getByName(String name) {
        UserType userType = userTypeRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(USER_TYPE_NOT_FOUND_NAME_MSG + name));
        return mapToResponseDTO(userType);
    }

    /**
     * Lista todos os tipos de usuários.
     *
     * @return lista de DTOs com todos os tipos
     */
    @Transactional(readOnly = true)
    public List<UserTypeResponseDTO> getAll() {
        return userTypeRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Atualiza um tipo de usuário existente.
     *
     * @param id ID do tipo a ser atualizado
     * @param requestDTO dados atualizados
     * @return DTO com dados do tipo atualizado
     * @throws ResourceNotFoundException se não encontrado
     */
    public UserTypeResponseDTO update(Long id, UserTypeRequestDTO requestDTO) {
        UserType userType = findUserTypeById(id);
        
        userType.setName(requestDTO.getName());
        userType.setDescription(requestDTO.getDescription());
        
        UserType updatedUserType = userTypeRepository.save(userType);
        return mapToResponseDTO(updatedUserType);
    }

    /**
     * Deleta um tipo de usuário.
     *
     * @param id ID do tipo a ser deletado
     * @throws ResourceNotFoundException se não encontrado
     */
    public void delete(Long id) {
        UserType userType = findUserTypeById(id);
        userTypeRepository.delete(userType);
    }

    /**
     * Encontra um tipo de usuário pelo ID com tratamento de erro.
     *
     * @param id ID do tipo
     * @return tipo encontrado
     * @throws ResourceNotFoundException se não encontrado
     */
    private UserType findUserTypeById(Long id) {
        return userTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(USER_TYPE_NOT_FOUND_ID_MSG + id));
    }

    /**
     * Mapeia entidade UserType para UserTypeResponseDTO.
     *
     * @param userType entidade a ser mapeada
     * @return DTO com dados da entidade
     */
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
