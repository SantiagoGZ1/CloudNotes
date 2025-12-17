package com.cloud.cloudnotes.auth;

import com.cloud.cloudnotes.user.UserEntity;
import com.cloud.cloudnotes.user.dto.LoginDto;
import com.cloud.cloudnotes.user.dto.RegisterDto;
import com.cloud.cloudnotes.user.dto.UserMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterDto req) {
        authService.register(req.getUsername(), req.getPassword());
        return "User registered";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDto req) {
        UserEntity loginEntity = UserMapper.toEntityLogin(req);
        if (loginEntity == null) {
            throw new IllegalArgumentException("Login request is null");
        }

        UserEntity user = authService.validateLogin(loginEntity.getUsername(), loginEntity.getPassword());

        return jwtService.generateToken(user.getUsername());
    }
}
