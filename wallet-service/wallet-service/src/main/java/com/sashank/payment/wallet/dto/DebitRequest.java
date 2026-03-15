package com.sashank.payment.wallet.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DebitRequest
{
    private String Email;
    private double amount;
}
