package com.sashank.payment.payment.exception;

public class PaymentFailedException extends RuntimeException
{
    public PaymentFailedException(String message)
    {
        super(message);
    }
}
