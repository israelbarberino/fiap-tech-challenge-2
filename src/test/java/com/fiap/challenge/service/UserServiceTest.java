package com.fiap.challenge.service;

import com.fiap.challenge.dto.AddressDTO;
import com.fiap.challenge.dto.ChangePasswordRequest;
import com.fiap.challenge.dto.LoginValidateRequest;
import com.fiap.challenge.dto.UserCreateRequest;
import com.fiap.challenge.dto.UserResponse;
import com.fiap.challenge.dto.UserUpdateRequest;
import com.fiap.challenge.entity.Address;
import com.fiap.challenge.entity.User;
import com.fiap.challenge.entity.UserRole;
import com.fiap.challenge.exception.DuplicateEmailException;
import com.fiap.challenge.exception.InvalidLoginException;
import com.fiap.challenge.exception.UserNotFoundException;
import com.fiap.challenge.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserCreateRequest createRequest;
    private AddressDTO addressDTO;

    @BeforeEach
    void setUp() {
        addressDTO = new AddressDTO(
            "Rua das Flores",
            "123",
            "Apto 456",
            "Sao Paulo",
            "SP",
            "01234-567"
        );

        user = new User();
        user.setId(1L);
        user.setName("Joao Silva");
        user.setEmail("joao@example.com");
        user.setLogin("joao.silva");
        user.setPasswordHash("hashedPassword");
        user.setRole(UserRole.CUSTOMER);
        user.setAddress(new Address("Rua das Flores", "123", "Apto 456", "Sao Paulo", "SP", "01234-567"));

        createRequest = new UserCreateRequest(
            "Joao Silva",
            "joao@example.com",
            "joao.silva",
            "senha123",
            UserRole.CUSTOMER,
            addressDTO
        );
    }

    @Test
    @DisplayName("Deve criar usuario com sucesso")
    void testCreateUserSuccess() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse response = userService.createUser(createRequest);

        assertNotNull(response);
        assertEquals("Joao Silva", response.getName());
        assertEquals("joao@example.com", response.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lancar excecao ao criar usuario com email duplicado")
    void testCreateUserWithDuplicateEmail() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        assertThrows(DuplicateEmailException.class, () -> userService.createUser(createRequest));
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lancar excecao ao criar usuario com login duplicado")
    void testCreateUserWithDuplicateLogin() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.of(user));

        assertThrows(DuplicateEmailException.class, () -> userService.createUser(createRequest));
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve atualizar usuario com sucesso")
    void testUpdateUserSuccess() {
        UserUpdateRequest updateRequest = new UserUpdateRequest(
            "Joao Silva Updated",
            "joao.updated@example.com",
            UserRole.RESTAURANT_OWNER,
            addressDTO
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("joao.updated@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse response = userService.updateUser(1L, updateRequest);

        assertNotNull(response);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar excecao ao atualizar usuario com email duplicado")
    void testUpdateUserWithDuplicateEmail() {
        UserUpdateRequest updateRequest = new UserUpdateRequest(
            "Joao Silva Updated",
            "duplicado@example.com",
            UserRole.RESTAURANT_OWNER,
            addressDTO
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("duplicado@example.com")).thenReturn(Optional.of(new User()));

        assertThrows(DuplicateEmailException.class, () -> userService.updateUser(1L, updateRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lancar excecao ao atualizar usuario inexistente")
    void testUpdateUserNotFound() {
        UserUpdateRequest updateRequest = new UserUpdateRequest(
            "Joao Silva Updated",
            "joao.updated@example.com",
            UserRole.RESTAURANT_OWNER,
            addressDTO
        );

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(1L, updateRequest));
    }

    @Test
    @DisplayName("Deve alterar senha com sucesso")
    void testChangePasswordSuccess() {
        ChangePasswordRequest changeRequest = new ChangePasswordRequest(
            "senha123",
            "novaSenha456",
            "novaSenha456"
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("senha123", user.getPasswordHash())).thenReturn(true);
        when(passwordEncoder.encode("novaSenha456")).thenReturn("newHashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        assertDoesNotThrow(() -> userService.changePassword(1L, changeRequest));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lancar excecao ao alterar senha com confirmacao incorreta")
    void testChangePasswordMismatch() {
        ChangePasswordRequest changeRequest = new ChangePasswordRequest(
            "senha123",
            "novaSenha456",
            "senhaErrada"
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("senha123", user.getPasswordHash())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.changePassword(1L, changeRequest));
    }

    @Test
    @DisplayName("Deve lancar excecao ao alterar senha com atual incorreta")
    void testChangePasswordWrongCurrent() {
        ChangePasswordRequest changeRequest = new ChangePasswordRequest(
            "senhaErrada",
            "novaSenha456",
            "novaSenha456"
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("senhaErrada", user.getPasswordHash())).thenReturn(false);

        assertThrows(InvalidLoginException.class, () -> userService.changePassword(1L, changeRequest));
    }

    @Test
    @DisplayName("Deve obter usuario por ID com sucesso")
    void testGetUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponse response = userService.getUserById(1L);

        assertNotNull(response);
        assertEquals("Joao Silva", response.getName());
    }

    @Test
    @DisplayName("Deve lancar excecao ao obter usuario inexistente")
    void testGetUserByIdNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    @DisplayName("Deve buscar usuarios por nome")
    void testGetUsersByName() {
        when(userRepository.findByNameContainingIgnoreCase("Joao"))
            .thenReturn(List.of(user));

        List<UserResponse> responses = userService.getUsersByName("Joao");

        assertEquals(1, responses.size());
        assertEquals("Joao Silva", responses.get(0).getName());
    }

    @Test
    @DisplayName("Deve deletar usuario com sucesso")
    void testDeleteUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> userService.deleteUser(1L));
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    @DisplayName("Deve validar login com sucesso")
    void testValidateLoginSuccess() {
        LoginValidateRequest request = new LoginValidateRequest("joao.silva", "senha123");

        when(userRepository.findByLogin("joao.silva")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("senha123", user.getPasswordHash())).thenReturn(true);

        assertDoesNotThrow(() -> userService.validateLogin(request));
    }

    @Test
    @DisplayName("Deve lancar excecao ao validar login com usuario inexistente")
    void testValidateLoginUserNotFound() {
        LoginValidateRequest request = new LoginValidateRequest("inexistente", "senha123");

        when(userRepository.findByLogin("inexistente")).thenReturn(Optional.empty());

        assertThrows(InvalidLoginException.class, () -> userService.validateLogin(request));
    }

    @Test
    @DisplayName("Deve lancar excecao ao validar login com senha incorreta")
    void testValidateLoginWrongPassword() {
        LoginValidateRequest request = new LoginValidateRequest("joao.silva", "senhaErrada");

        when(userRepository.findByLogin("joao.silva")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("senhaErrada", user.getPasswordHash())).thenReturn(false);

        assertThrows(InvalidLoginException.class, () -> userService.validateLogin(request));
    }
}
