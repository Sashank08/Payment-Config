package com.sashank.payment.wallet.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreditRequest
{
    private String email;
    private double amount;
}
