package com.sashank.payment.payment.service;

import com.sashank.payment.payment.client.NotificationClient;
import com.sashank.payment.payment.client.WalletClient;
import com.sashank.payment.payment.dto.CreditRequest;
import com.sashank.payment.payment.dto.DebitRequest;
import com.sashank.payment.payment.dto.NotificationRequest;
import com.sashank.payment.payment.dto.PaymentRequest;
import com.sashank.payment.payment.entity.Transaction;
import com.sashank.payment.payment.exception.PaymentFailedException;
import com.sashank.payment.payment.repo.TransactionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService
{
    private final TransactionRepo transactionRepo;
    private final WalletClient walletClient;
    private final NotificationClient notificationClient;

    public Transaction pay(String senderEmail, PaymentRequest paymentRequest) {

        if(senderEmail.equals(paymentRequest.getReceiverMail())) {
            throw new PaymentFailedException("Sender and receiver cannot be the same");
        }

        if(paymentRequest.getAmount() <= 0) {
            throw new PaymentFailedException("Amount must be greater than zero");
        }

        DebitRequest debitRequest = new DebitRequest();
        debitRequest.setEmail(senderEmail);
        debitRequest.setAmount(paymentRequest.getAmount());

        CreditRequest creditRequest = new CreditRequest();
        creditRequest.setEmail(paymentRequest.getReceiverMail());
        creditRequest.setAmount(paymentRequest.getAmount());

        boolean debited = false;

        try {

            walletClient.debit(debitRequest);
            debited = true;

            walletClient.credit(creditRequest);

        } catch (Exception e) {

            if(debited){
                CreditRequest refundRequest = new CreditRequest();
                refundRequest.setEmail(senderEmail);
                refundRequest.setAmount(paymentRequest.getAmount());

                walletClient.credit(refundRequest);
            }

            throw new PaymentFailedException("Payment failed.");
        }

        Transaction transaction = Transaction.builder()
                .senderEmail(senderEmail)
                .receiverEmail(paymentRequest.getReceiverMail())
                .amount(paymentRequest.getAmount())
                .status("SUCCESS")
                .createdAt(LocalDateTime.now())
                .build();

        Transaction savedTransaction = transactionRepo.save(transaction);

        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setSenderMail(senderEmail);
        notificationRequest.setReceiverMail(paymentRequest.getReceiverMail());
        notificationRequest.setAmount(paymentRequest.getAmount());
        notificationRequest.setTransactionId(savedTransaction.getId().toString());

        notificationClient.sendNotification(notificationRequest);

        return savedTransaction;
    }
    public List<Transaction> getHistory(String email) {
        return transactionRepo.findBySenderEmailOrReceiverEmail(email, email);
    }
}