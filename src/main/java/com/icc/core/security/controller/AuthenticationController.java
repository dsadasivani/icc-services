package com.icc.core.security.controller;

import com.icc.core.security.model.AuthenticateRequest;
import com.icc.core.security.model.AuthenticationResponse;
import com.icc.core.security.model.RegisterRequest;
import com.icc.core.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

//    @GetMapping(value = "/status")
//    public ResponseEntity<Object> testApiCall() throws JsonProcessingException {
//        ObjectMapper mapper = new ObjectMapper();
//        return new ResponseEntity<Object>(mapper.readTree("{\"status\": \"HealthCheck Success\"}"), HttpStatus.OK);
//    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticateRequest request
    ) {
        return service.authenticate(request);
    }
}
