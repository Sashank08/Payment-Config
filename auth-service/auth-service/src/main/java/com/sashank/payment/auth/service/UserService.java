package com.sashank.payment.auth.service;

import com.sashank.payment.auth.dto.LoginRequest;
import com.sashank.payment.auth.dto.RegisterRequest;
import com.sashank.payment.auth.entity.User;
import com.sashank.payment.auth.exception.EmailAlreadyExistsException;
import com.sashank.payment.auth.exception.InvalidCredentialsException;
import com.sashank.payment.auth.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService
{
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    public User registerUser(RegisterRequest request)
    {
        if(userRepo.findByEmail(request.getEmail()).isPresent())
        {
            throw new EmailAlreadyExistsException("This Email already exists!!");
        }
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("USER")
                .build();

        return userRepo.save(user);
    }
    public String loginUser(LoginRequest request)
    {
        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(()-> new InvalidCredentialsException("Invalid Email or Password"));
        if(!passwordEncoder.matches(request.getPassword(),user.getPassword()))
        {
            throw new InvalidCredentialsException("Invalid Email or Password");
        }
        return jwtService.generateToken(user);
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public void deleteUserByEmail(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userRepo.delete(user);
    }
}