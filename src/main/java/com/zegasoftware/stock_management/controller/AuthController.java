package com.zegasoftware.stock_management.controller;

import com.zegasoftware.stock_management.jwt.JwtService;
import com.zegasoftware.stock_management.model.dto.user.UserDetailsDto;
import com.zegasoftware.stock_management.model.entity.User;
import com.zegasoftware.stock_management.model.enums.UserRoles;
import com.zegasoftware.stock_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Autowired
    public AuthController(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, UserDetailsService userDetailsService) {

        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/register")
    public AuthenticationResponse saveUser(@RequestBody UserDetailsDto userDetailsDto) {
        var user = User.builder()
                .username(userDetailsDto.getUsername())
                .password(passwordEncoder.encode(userDetailsDto.getPassword()))
                .role(UserRoles.USER)
                .build();
        userRepository.save(user);
        var token = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(token).build();
    }

    @PostMapping("/authenticate")
    public AuthenticationResponse authenticate(@RequestBody UserDetailsDto userDetailsDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDetailsDto.getUsername(), userDetailsDto.getPassword()));
        UserDetails user = userDetailsService.loadUserByUsername(userDetailsDto.getUsername());
        String token = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(token).build();
    }
}
