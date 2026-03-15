package com.sashank.payment.wallet.exception;

public class WalletNotFoundException extends RuntimeException
{
    public WalletNotFoundException(String message)
    {
        super(message);
    }
}
