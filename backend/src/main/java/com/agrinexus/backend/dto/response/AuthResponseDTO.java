package com.agrinexus.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AuthResponseDTO {
    private String accessToken;
    private String refreshToken;
}