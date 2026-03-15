package com.sashank.payment.auth.controller;

import com.sashank.payment.auth.dto.LoginRequest;
import com.sashank.payment.auth.dto.RegisterRequest;
import com.sashank.payment.auth.entity.User;
import com.sashank.payment.auth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")

public class AuthController
{
    private final UserService userService;
    public AuthController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/register")
    public User register(@Valid @RequestBody RegisterRequest registerRequest)
    {
        return userService.registerUser(registerRequest);
    }
    @PostMapping("/login")
    public Map<String,String> login(@Valid @RequestBody LoginRequest loginRequest)
    {
       String token = userService.loginUser(loginRequest);
       return Map.of("token",token);
    }

    // ADMIN: Get all users
    @PreAuthorize("hasRole('Admin')")
    @GetMapping("/admin/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // ADMIN: Delete user by email
    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping("/admin/delete/{email}")
    public String deleteUserByEmail(@PathVariable String email) {
        userService.deleteUserByEmail(email);
        return "User deleted successfully";
    }

}
