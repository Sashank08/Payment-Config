package com.sashank.payment.wallet.exception;

public class InsufficientBalanceException extends RuntimeException
{
    public InsufficientBalanceException(String message)
    {
        super(message);
    }
}
