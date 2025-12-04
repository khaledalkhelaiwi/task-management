package com.task_management_mini_system.task_management.controller;

import com.task_management_mini_system.task_management.model.User;
import com.task_management_mini_system.task_management.security.LoginRequest;
import com.task_management_mini_system.task_management.security.LoginResponse;
import com.task_management_mini_system.task_management.service.UserService;
import com.task_management_mini_system.task_management.config.JwtUtil;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService,
                          AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

  
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User saved = userService.registerUser(
                user.getUsername(),
                user.getEmail(),
                user.getPassword()
        );
        saved.setPassword("..."); 
        return ResponseEntity.ok(saved);
    }

    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

    	
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        
        String token = jwtUtil.generateToken(request.getUsername());

         return ResponseEntity.ok(new LoginResponse(token));
    }
}
