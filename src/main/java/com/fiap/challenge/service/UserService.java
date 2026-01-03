package com.fiap.challenge.service;

import com.fiap.challenge.dto.*;
import com.fiap.challenge.entity.Address;
import com.fiap.challenge.entity.User;
import com.fiap.challenge.exception.DuplicateEmailException;
import com.fiap.challenge.exception.InvalidLoginException;
import com.fiap.challenge.exception.UserNotFoundException;
import com.fiap.challenge.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateEmailException("Email '" + request.getEmail() + "' já está registrado");
        }
        
        if (userRepository.findByLogin(request.getLogin()).isPresent()) {
            throw new DuplicateEmailException("Login '" + request.getLogin() + "' já está em uso");
        }
        
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setLogin(request.getLogin());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        
        if (request.getAddress() != null) {
            AddressDTO addrDTO = request.getAddress();
            user.setAddress(new Address(
                addrDTO.getStreet(),
                addrDTO.getNumber(),
                addrDTO.getComplement(),
                addrDTO.getCity(),
                addrDTO.getState(),
                addrDTO.getZipCode()
            ));
        }
        
        User savedUser = userRepository.save(user);
        return new UserResponse(savedUser);
    }

    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("Usuário com ID " + id + " não encontrado"));
        
        if (!user.getEmail().equals(request.getEmail()) &&
            userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateEmailException("Email '" + request.getEmail() + "' já está registrado");
        }
        
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        
        if (request.getAddress() != null) {
            AddressDTO addrDTO = request.getAddress();
            user.setAddress(new Address(
                addrDTO.getStreet(),
                addrDTO.getNumber(),
                addrDTO.getComplement(),
                addrDTO.getCity(),
                addrDTO.getState(),
                addrDTO.getZipCode()
            ));
        }
        
        User updated = userRepository.save(user);
        return new UserResponse(updated);
    }


    @Transactional
    public void changePassword(Long id, ChangePasswordRequest request) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("Usuário com ID " + id + " não encontrado"));
        
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new InvalidLoginException("Senha atual está incorreta");
        }
        
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Nova senha e confirmação não conferem");
        }
        
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }


    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("Usuário com ID " + id + " não encontrado"));
        return new UserResponse(user);
    }


    public List<UserResponse> getUsersByName(String name) {
        return userRepository.findByNameContainingIgnoreCase(name)
            .stream()
            .map(UserResponse::new)
            .collect(Collectors.toList());
    }


    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("Usuário com ID " + id + " não encontrado"));
        userRepository.delete(user);
    }


    public void validateLogin(LoginValidateRequest request) {
        User user = userRepository.findByLogin(request.getLogin())
            .orElseThrow(() -> new InvalidLoginException("Login ou senha inválidos"));
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new InvalidLoginException("Login ou senha inválidos");
        }
    }
}
