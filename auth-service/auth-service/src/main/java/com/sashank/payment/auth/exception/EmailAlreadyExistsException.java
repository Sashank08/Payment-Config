package com.sashank.payment.auth.exception;

public class EmailAlreadyExistsException extends RuntimeException
{
    public EmailAlreadyExistsException(String message)
    {
        super(message);
    }
}