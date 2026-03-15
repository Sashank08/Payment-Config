package com.sashank.payment.wallet.repo;

import com.sashank.payment.wallet.entity.Wallet;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WalletRepo extends JpaRepository<Wallet,Long>
{
    Optional<Wallet> findByEmail(String email);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM Wallet w WHERE w.email = :email")
    Optional<Wallet> findByEmailForUpdate(@Param("email") String email);
}
