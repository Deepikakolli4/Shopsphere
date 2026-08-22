package com.shopsphere.ecommerce.controller.Auth;

import com.shopsphere.ecommerce.dto.Auth.LoginRequestDTO;
import com.shopsphere.ecommerce.dto.Auth.LoginResponseDTO;
import com.shopsphere.ecommerce.dto.Auth.RegisterRequestDTO;
import com.shopsphere.ecommerce.service.Auth.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public String register(
            @Valid @RequestBody RegisterRequestDTO request) {

        authService.register(request);

        return "User registered successfully";
    }

    @PostMapping("/login")
    public LoginResponseDTO login(
            @Valid @RequestBody LoginRequestDTO request) {

        return authService.login(request);
    }
}