package com.carrito.userservice.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carrito.userservice.dto.UserRequestDTO;
import com.carrito.userservice.dto.UserResponseDTO;
import com.carrito.userservice.model.User;
import com.carrito.userservice.security.JwtTokenProvider;
import com.carrito.userservice.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        User user = userService.registerUser(userRequestDTO);
        String token = jwtTokenProvider.generateToken(user);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("email", user.getEmail());
        response.put("fullName", user.getFirstName() + " " + user.getLastName());
        response.put("address", user.getAddress());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.get("email"),
                        loginRequest.get("password")));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userService.findByEmail(loginRequest.get("email"))
                .orElseThrow(() -> new RuntimeException(
                        "Usuario no encontrado con el correo: " + loginRequest.get("email")));
        String token = jwtTokenProvider.generateToken(user);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("email", user.getEmail());
        response.put("fullName", user.getFirstName() + " " + user.getLastName());
        response.put("address", user.getAddress());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMyProfile(HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        String userId = jwtTokenProvider.getUserIdFromJWT(token);

        UserResponseDTO response = userService.getUserProfileById(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/exists")
    public ResponseEntity< Map<String, String>> existsByEmail(@RequestBody Map<String, String> request) {
       String email = request.get("email");
       this.userService.existsByEmail(email);
       return ResponseEntity.ok(request);
    }
}