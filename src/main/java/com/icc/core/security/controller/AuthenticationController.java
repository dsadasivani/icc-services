package com.icc.core.security.controller;

import com.icc.core.security.model.AuthenticateRequest;
import com.icc.core.security.model.AuthenticationResponse;
import com.icc.core.security.model.CustomResponse;
import com.icc.core.security.model.RegisterRequest;
import com.icc.core.security.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "JWT authentication APIs")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    @Operation(
        summary = "Register new user",
        description = "Creates a new user with provided details like name, email & password. The response is a jwt token corresponding to user.",
        tags = { "authentication", "post" })
    @PostMapping("/auth/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return service.register(request);
    }

    @Operation(
            summary = "Updates existing user details",
            description = "updates existing user's details like like first name, last name & password(email cannot be updated). The response is a jwt token corresponding to user with updated details.",
            tags = { "authentication", "post" })
    @PostMapping("/profileUpdate")
    public ResponseEntity<CustomResponse> profileUpdate(
            @RequestBody RegisterRequest request
    ) {
        return service.profileUpdate(request);
    }
    @Operation(
            summary = "Authenticates a user",
            description = "Authenticates a user with provided username & password. The response would be a jwt token on successful login",
            tags = { "authentication", "post" })
    @PostMapping("/auth/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticateRequest request
    ) {
        return service.authenticate(request);
    }
}
