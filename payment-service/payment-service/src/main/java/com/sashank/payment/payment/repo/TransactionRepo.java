package com.sashank.payment.payment.repo;

import com.sashank.payment.payment.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepo extends JpaRepository<Transaction,Long>
{
    List<Transaction> findBySenderEmailOrReceiverEmail(String sender, String receiver);
}
