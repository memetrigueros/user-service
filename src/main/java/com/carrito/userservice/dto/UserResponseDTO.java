package com.carrito.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private LocalDate birthDate;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
