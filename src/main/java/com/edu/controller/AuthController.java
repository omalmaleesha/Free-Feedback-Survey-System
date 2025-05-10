package com.edu.controller;

import com.edu.dto.UserDto;
import com.edu.entity.User;
import com.edu.security.JwtUtil;
import com.edu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.registerUser(userDto));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDto userDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword())
        );
        UserDetails userDetails = userService.loadUserByUsername(userDto.getUsername());
        String token = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(token);
    }
}