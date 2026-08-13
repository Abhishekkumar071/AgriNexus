package com.agrinexus.backend.service;

import com.agrinexus.backend.dto.request.RegisterRequestDTO;
import com.agrinexus.backend.dto.response.UserResponseDTO;

public interface AuthService {

    UserResponseDTO register(RegisterRequestDTO requestDTO);
}