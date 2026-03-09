package com.sfp.controller;

import com.sfp.dto.request.LoginRequestDTO;
import com.sfp.dto.request.RegisterRequestDTO;
import com.sfp.dto.response.AuthResponseDTO;
import com.sfp.dto.response.UserResponseDTO;
import com.sfp.service.AuthService;
import com.sfp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody RegisterRequestDTO dto) {
        UserResponseDTO user = userService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        AuthResponseDTO authResponse = authService.authenticate(dto);
        return ResponseEntity.ok(authResponse);
    }
}