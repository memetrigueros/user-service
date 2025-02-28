package com.carrito.userservice.service;

import com.carrito.userservice.dto.UserRequestDTO;
import com.carrito.userservice.dto.UserResponseDTO;
import com.carrito.userservice.model.User;
import com.carrito.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Register a new user if the email is not in use.
     *
     * @param userRequestDTO User data to register.
     * @return UserResponseDTO with the registered user information.
     * @throws IllegalArgumentException if the email is already registered.
     */
    @Override
    public User registerUser(UserRequestDTO userRequestDTO) {
        if (userRepository.existsByEmail(userRequestDTO.getEmail())) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado.");
        }

        User user = User.builder()
                .firstName(userRequestDTO.getFirstName())
                .lastName(userRequestDTO.getLastName())
                .address(userRequestDTO.getAddress())
                .email(userRequestDTO.getEmail())
                .birthDate(userRequestDTO.getBirthDate())
                .password(passwordEncoder.encode(userRequestDTO.getPassword()))
                .build();

        User savedUser = userRepository.save(user);

        return savedUser;
    }

    /**
     * Search for a user by their email.
     *
     * @param User's email.
     * @return User if it exists, or empty if it is not found.
     */
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Search for a user by their id.
     *
     * @param userId: User's id.
     * @return UserResponseDTO if it exists.
     * @throws RuntimeException if the user does not exist.
     */
    @Override
    public UserResponseDTO getUserProfileById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return UserResponseDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .address(user.getAddress())
                .email(user.getEmail())
                .birthDate(user.getBirthDate())
                .createdAt(user.getCreatedAt().toLocalDate())
                .updatedAt(user.getUpdatedAt().toLocalDate())
                .build();
    }

    /**
     * Validate if a user exists through email.
     *
     * @param User's email.
     * @return UserResponseDTO if it exists.
     * @throws RuntimeException if the user does not exist.
     */
    @Override
    public UserResponseDTO existsByEmail(String email) {
       User user = this.findByEmail(email)
                .orElseThrow(() -> new RuntimeException(
                        "Usuario no encontrado con el correo: " + email));

        return UserResponseDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .address(user.getAddress())
                .email(user.getEmail())
                .birthDate(user.getBirthDate())
                .createdAt(user.getCreatedAt().toLocalDate())
                .updatedAt(user.getUpdatedAt().toLocalDate())
                .build();
    }

}
