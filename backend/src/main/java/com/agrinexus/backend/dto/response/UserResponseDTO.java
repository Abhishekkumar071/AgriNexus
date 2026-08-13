package com.agrinexus.backend.dto.response;

import com.agrinexus.backend.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserResponseDTO {

    private String id;
    private String name;
    private String email;
    private Role role;
    private String phoneNumber;
    private Instant createdAt;
}