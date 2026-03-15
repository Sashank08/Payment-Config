package com.sashank.payment.notification.service;

import com.sashank.payment.notification.dto.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;

    public void sendPaymentEmail(NotificationRequest request) {

        String message = """
                Payment Successful
                
                Transaction Details:
                --------------------------------
                Sender: %s
                Receiver: %s
                Amount: ₹%.2f
                Transaction ID: %s
                --------------------------------
                Thank you for using our payment service!
                """
                .formatted(
                        request.getSenderMail(),
                        request.getReceiverMail(),
                        request.getAmount(),
                        request.getTransactionId()
                );

        // Send to sender
        SimpleMailMessage senderMail = new SimpleMailMessage();
        senderMail.setTo(request.getSenderMail());
        senderMail.setSubject("Payment Successful");
        senderMail.setText(message);

        mailSender.send(senderMail);

        // Send to receiver
        SimpleMailMessage receiverMail = new SimpleMailMessage();
        receiverMail.setTo(request.getReceiverMail());
        receiverMail.setSubject("You Received Money");
        receiverMail.setText(message);

        mailSender.send(receiverMail);
    }
}