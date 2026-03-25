package com.fiap.challenge.service;

import com.fiap.challenge.dto.UserTypeRequestDTO;
import com.fiap.challenge.dto.UserTypeResponseDTO;
import com.fiap.challenge.entity.UserType;
import com.fiap.challenge.exception.ResourceNotFoundException;
import com.fiap.challenge.repository.UserTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários - UserTypeService")
class UserTypeServiceTest {

    @Mock
    private UserTypeRepository userTypeRepository;

    @InjectMocks
    private UserTypeService userTypeService;

    private UserType userType;
    private UserTypeRequestDTO requestDTO;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        userType = new UserType("Dono de Restaurante", "Usuário que possui um restaurante");
        userType.setId(1L);
        userType.setCreatedAt(now);
        userType.setLastModifiedAt(now);

        requestDTO = new UserTypeRequestDTO("Dono de Restaurante", "Usuário que possui um restaurante");
    }

    @Test
    @DisplayName("Deve criar um tipo de usuário com sucesso")
    void testCreateUserType() {
        when(userTypeRepository.save(any(UserType.class))).thenReturn(userType);

        UserTypeResponseDTO responseDTO = userTypeService.create(requestDTO);

        assertNotNull(responseDTO);
        assertEquals("Dono de Restaurante", responseDTO.name());
        assertEquals("Usuário que possui um restaurante", responseDTO.description());
        verify(userTypeRepository, times(1)).save(any(UserType.class));
    }

    @Test
    @DisplayName("Deve buscar tipo de usuário por ID com sucesso")
    void testGetUserTypeById() {
        when(userTypeRepository.findById(1L)).thenReturn(Optional.of(userType));

        UserTypeResponseDTO responseDTO = userTypeService.getById(1L);

        assertNotNull(responseDTO);
        assertEquals(1L, responseDTO.id());
        assertEquals("Dono de Restaurante", responseDTO.name());
        verify(userTypeRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção quando tipo de usuário não encontrado por ID")
    void testGetUserTypeByIdNotFound() {
        when(userTypeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userTypeService.getById(999L));
        verify(userTypeRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Deve buscar tipo de usuário por nome com sucesso")
    void testGetUserTypeByName() {
        when(userTypeRepository.findByName("Dono de Restaurante")).thenReturn(Optional.of(userType));

        UserTypeResponseDTO responseDTO = userTypeService.getByName("Dono de Restaurante");

        assertNotNull(responseDTO);
        assertEquals("Dono de Restaurante", responseDTO.name());
        verify(userTypeRepository, times(1)).findByName("Dono de Restaurante");
    }

    @Test
    @DisplayName("Deve lançar exceção quando tipo de usuário não encontrado por nome")
    void testGetUserTypeByNameNotFound() {
        when(userTypeRepository.findByName("Inexistente")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userTypeService.getByName("Inexistente"));
        verify(userTypeRepository, times(1)).findByName("Inexistente");
    }

    @Test
    @DisplayName("Deve listar todos os tipos de usuários")
    void testGetAllUserTypes() {
        List<UserType> userTypes = new ArrayList<>();
        userTypes.add(userType);
        when(userTypeRepository.findAll()).thenReturn(userTypes);

        List<UserTypeResponseDTO> responseDTOs = userTypeService.getAll();

        assertNotNull(responseDTOs);
        assertEquals(1, responseDTOs.size());
        assertEquals("Dono de Restaurante", responseDTOs.get(0).name());
        verify(userTypeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve atualizar um tipo de usuário com sucesso")
    void testUpdateUserType() {
        UserTypeRequestDTO updateDTO = new UserTypeRequestDTO("Atualizado", "Descrição atualizada");
        when(userTypeRepository.findById(1L)).thenReturn(Optional.of(userType));
        when(userTypeRepository.save(any(UserType.class))).thenReturn(userType);

        UserTypeResponseDTO responseDTO = userTypeService.update(1L, updateDTO);

        assertNotNull(responseDTO);
        verify(userTypeRepository, times(1)).findById(1L);
        verify(userTypeRepository, times(1)).save(any(UserType.class));
    }

    @Test
    @DisplayName("Deve deletar um tipo de usuário com sucesso")
    void testDeleteUserType() {
        when(userTypeRepository.findById(1L)).thenReturn(Optional.of(userType));

        userTypeService.delete(1L);

        verify(userTypeRepository, times(1)).findById(1L);
        verify(userTypeRepository, times(1)).delete(userType);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar tipo de usuário inexistente")
    void testDeleteUserTypeNotFound() {
        when(userTypeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userTypeService.delete(999L));
        verify(userTypeRepository, times(1)).findById(999L);
    }
}
