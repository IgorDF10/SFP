package com.sfp.service;

import com.sfp.dto.request.LoginRequestDTO;
import com.sfp.dto.response.AuthResponseDTO;
import com.sfp.exception.BusinessException;
import com.sfp.model.User;
import com.sfp.repository.UserRepository;
import com.sfp.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional(readOnly = true)
    public AuthResponseDTO authenticate(LoginRequestDTO dto) {
        // Busca o usuário
        User user = userRepository.findByEmailAndIsActiveTrue(dto.getEmail())
                .orElseThrow(() -> new BusinessException("Credenciais inválidas"));

        // Verifica a senha
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException("Credenciais inválidas");
        }

        // Gera o token JWT
        String token = jwtTokenProvider.generateToken(user.getEmail());

        // Retorna a resposta
        return AuthResponseDTO.builder()
                .token(token)
                .type("Bearer")
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}