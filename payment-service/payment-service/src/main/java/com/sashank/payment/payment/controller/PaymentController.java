package com.sashank.payment.payment.controller;

import com.sashank.payment.payment.dto.PaymentRequest;
import com.sashank.payment.payment.entity.Transaction;
import com.sashank.payment.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/pay")
    public Transaction pay(
            @RequestHeader("X-User-Email") String senderEmail,
            @RequestBody PaymentRequest request
    ) {

        return paymentService.pay(senderEmail, request);
    }
    @GetMapping("/history")
    public List<Transaction> getHistory(@RequestHeader("X-User-Email") String email) {
        return paymentService.getHistory(email);
    }
}