package com.carrito.userservice.service;

import com.carrito.userservice.dto.UserRequestDTO;
import com.carrito.userservice.dto.UserResponseDTO;
import com.carrito.userservice.model.User;

import java.util.Optional;

public interface UserService {

    User registerUser(UserRequestDTO userRequestDTO);
    Optional<User> findByEmail(String email);
    public UserResponseDTO getUserProfileById(String userId);
    public UserResponseDTO existsByEmail(String email);

}
