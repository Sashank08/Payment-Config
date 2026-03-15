package com.sashank.payment.payment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DebitRequest
{
    private String email;
    private double amount;
}
