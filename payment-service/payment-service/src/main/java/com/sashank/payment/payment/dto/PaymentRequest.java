package com.sashank.payment.payment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest
{
    private String receiverMail;
    private double amount;
}
