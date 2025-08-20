package com.blog.auth.dto;

import com.blog.security.JwtUtil;
import com.blog.user.User;
import com.blog.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtUtil jwt;

    public void register(AuthDtos.RegisterRequest r){
        if(userRepo.existsByUsername(r.username()) || userRepo.existsByEmail(r.email()))
            throw new IllegalArgumentException("Username or email address already in use");
        var user = User.builder()
                .username(r.username())
                .email(r.email())
                .password(encoder.encode(r.password()))
                .build();
        userRepo.save(user);
    }

    public AuthDtos.JwtResponse login(AuthDtos.LoginRequest r) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(r.username(), r.password()));
        var token = jwt.generateToken(r.username());
        return new AuthDtos.JwtResponse(token, r.username());
    }
}
