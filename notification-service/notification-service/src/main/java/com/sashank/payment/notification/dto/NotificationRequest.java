package com.sashank.payment.notification.dto;

import lombok.Data;

@Data
public class NotificationRequest
{
    private String senderMail;
    private String receiverMail;
    private double amount;
    private String transactionId;
}
