package com.sashank.payment.wallet.service;

import com.sashank.payment.wallet.entity.Wallet;
import com.sashank.payment.wallet.exception.InsufficientBalanceException;
import com.sashank.payment.wallet.exception.WalletNotFoundException;
import com.sashank.payment.wallet.repo.WalletRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletService
{
    private final WalletRepo walletRepo;

    public Double getBalance(String email)
    {
        Wallet wallet = walletRepo.findByEmail(email).orElseThrow( () -> new WalletNotFoundException("Wallet not found!!"));
        return wallet.getBalance();
    }

    public Double credit(String email, Double amount)
    {
        Wallet wallet = walletRepo.findByEmail(email)
                .orElseThrow(() -> new WalletNotFoundException("Receiver wallet not found"));

        wallet.setBalance(wallet.getBalance()+amount);
        walletRepo.save(wallet);
        return wallet.getBalance();
    }

    @Transactional
    public Double debit(String email, Double amount)
    {
        System.out.println("Debit request for " + email + " amount " + amount);
        Wallet wallet = walletRepo.findByEmailForUpdate(email)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found!!"));

        if(wallet.getBalance()<amount)
        {
            throw new InsufficientBalanceException("Insufficient Balance!!");
        }

        wallet.setBalance(wallet.getBalance()-amount);
        walletRepo.save(wallet);
        return wallet.getBalance();
    }

    public void createWallet(String email) {

        if (walletRepo.findByEmail(email).isPresent()) {
            throw new WalletNotFoundException("Wallet already exists");
        }

        Wallet wallet = Wallet.builder()
                .email(email)
                .balance(0.0)
                .build();

        walletRepo.save(wallet);
    }
}
