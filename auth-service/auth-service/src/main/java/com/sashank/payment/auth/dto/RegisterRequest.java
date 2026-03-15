package com.sashank.payment.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest
{
    @Email
    @NotNull
    private String email;
    @NotNull
    private String password;
}
