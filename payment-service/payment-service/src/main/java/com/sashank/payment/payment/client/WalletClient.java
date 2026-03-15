package com.sashank.payment.payment.client;

import com.sashank.payment.payment.dto.CreditRequest;
import com.sashank.payment.payment.dto.DebitRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@FeignClient(name = "wallet-service")
public interface WalletClient
{
    @PostMapping("/wallet/debit")
    Double debit(@RequestBody DebitRequest request);

    @PostMapping("/wallet/credit")
    Double credit(@RequestBody CreditRequest request);
}
