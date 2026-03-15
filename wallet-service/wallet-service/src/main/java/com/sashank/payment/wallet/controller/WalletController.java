package com.sashank.payment.wallet.controller;

import com.sashank.payment.wallet.dto.CreditRequest;
import com.sashank.payment.wallet.dto.DebitRequest;
import com.sashank.payment.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @GetMapping("/balance")
    public Double getBalance(@RequestHeader("X-User-Email") String email) {
        return walletService.getBalance(email);
    }

    @PostMapping("/credit")
    public Double credit(@RequestBody CreditRequest request) {
        return walletService.credit(request.getEmail(), request.getAmount());
    }

    @PostMapping("/debit")
    public Double debit(@RequestBody DebitRequest request) {
        return walletService.debit(request.getEmail(), request.getAmount());
    }

    @PostMapping("/create")
    public String createWallet(@RequestHeader("X-User-Email") String email) {
        walletService.createWallet(email);
        return "Wallet created successfully";
    }

    //@PreAuthorize("hasRole('Admin')")
    @PostMapping("/admin/credit")
    public Double adminCredit(@RequestBody CreditRequest request) {

        return walletService.credit(request.getEmail(), request.getAmount());
    }
}