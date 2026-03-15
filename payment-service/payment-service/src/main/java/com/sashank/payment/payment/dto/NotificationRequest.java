package com.sashank.payment.payment.dto;

import lombok.Data;

@Data
public class NotificationRequest
{
    private String senderMail;
    private String receiverMail;
    private double amount;
    private String transactionId;
}
