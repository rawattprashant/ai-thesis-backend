package com.radiantSKill.infiniti.controllers;

import com.radiantSKill.infiniti.dto.ApiResponse;
import com.radiantSKill.infiniti.dto.LoginRequest;
import com.radiantSKill.infiniti.dto.RegisterRequestDTO;
import com.radiantSKill.infiniti.dto.UserResponseDTO;
import com.radiantSKill.infiniti.entity.AppUser;
import com.radiantSKill.infiniti.repository.AppUserRepository;
import com.radiantSKill.infiniti.services.UserService;
import com.radiantSKill.infiniti.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AppUserRepository appUserRepository;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@RequestBody RegisterRequestDTO request) {

        userService.registerUser(request);

        return ResponseEntity.ok(
                new ApiResponse<>("success", "User registered successfully", null)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, String>>> login(
            @RequestBody LoginRequest loginRequest) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        // ✅ fetch user to get role
        AppUser user = appUserRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String role = user.getRoles()
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Role not assigned"))
                .getName();

        // ✅ generate token with role
        String token = jwtUtil.generateToken(user.getEmail(), role);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "success",
                        "Login successful",
                        Map.of("token", token)
                )
        );
    }

    @GetMapping("/user")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUser(Authentication auth) {

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse<>("error", "Unauthorized", null));
        }

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "success",
                        "User fetched",
                        userService.getUserByEmail(auth.getName())
                )
        );
    }
}