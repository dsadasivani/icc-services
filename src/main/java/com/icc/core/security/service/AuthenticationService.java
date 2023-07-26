package com.icc.core.security.service;

import com.icc.core.security.model.AuthenticateRequest;
import com.icc.core.security.model.AuthenticationResponse;
import com.icc.core.security.model.RegisterRequest;
import com.icc.core.security.model.user.Role;
import com.icc.core.security.model.user.User;
import com.icc.core.security.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .userPassword(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public ResponseEntity<AuthenticationResponse> authenticate(AuthenticateRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            var user = repository.findByEmail(request.getEmail())
                    .orElseThrow();
            var jwtToken = jwtService.generateToken(user);
            return ResponseEntity.ok(AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build());
        } catch (AuthenticationException ae) {
            return new ResponseEntity<>(AuthenticationResponse.builder()
                    .error("Invalid Credentials")
                    .build(), HttpStatus.NOT_FOUND);
        }

    }
}
