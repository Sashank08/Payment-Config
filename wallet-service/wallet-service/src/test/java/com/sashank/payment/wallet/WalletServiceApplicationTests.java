package com.sashank.payment.wallet;

import com.sashank.payment.wallet.entity.Wallet;
import com.sashank.payment.wallet.service.WalletService;
import com.sashank.payment.wallet.exception.InsufficientBalanceException;
import com.sashank.payment.wallet.exception.WalletNotFoundException;
import com.sashank.payment.wallet.repo.WalletRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

	@Mock
	private WalletRepo walletRepo;
	@InjectMocks
	private WalletService walletService;

	//Test get balance success
	@Test
	void testGetBalance() {

		Wallet wallet = Wallet.builder()
				.email("user@gmail.com")
				.balance(1000.0)
				.build();

		when(walletRepo.findByEmail("user@gmail.com"))
				.thenReturn(Optional.of(wallet));

		Double balance = walletService.getBalance("user@gmail.com");

		assertEquals(1000.0, balance);
	}

	//Test credit success
	@Test
	void testCreditSuccess() {

		Wallet wallet = Wallet.builder()
				.email("user@gmail.com")
				.balance(500.0)
				.build();

		when(walletRepo.findByEmail("user@gmail.com"))
				.thenReturn(Optional.of(wallet));

		when(walletRepo.save(any(Wallet.class)))
				.thenReturn(wallet);

		Double newBalance = walletService.credit("user@gmail.com", 200.0);

		assertEquals(700.0, newBalance);

		verify(walletRepo).save(wallet);
	}

	//Test debit success
	@Test
	void testDebitSuccess() {

		Wallet wallet = Wallet.builder()
				.email("user@gmail.com")
				.balance(1000.0)
				.build();

		when(walletRepo.findByEmailForUpdate("user@gmail.com"))
				.thenReturn(Optional.of(wallet));

		when(walletRepo.save(any(Wallet.class)))
				.thenReturn(wallet);

		Double newBalance = walletService.debit("user@gmail.com", 300.0);

		assertEquals(700.0, newBalance);

		verify(walletRepo).save(wallet);
	}

	//Test insufficient balance
	@Test
	void testDebitInsufficientBalance() {

		Wallet wallet = Wallet.builder()
				.email("user@gmail.com")
				.balance(100.0)
				.build();

		when(walletRepo.findByEmailForUpdate("user@gmail.com"))
				.thenReturn(Optional.of(wallet));

		assertThrows(
				InsufficientBalanceException.class,
				() -> walletService.debit("user@gmail.com", 500.0)
		);
	}

	//Test wallet creation
	@Test
	void testCreateWallet() {

		when(walletRepo.findByEmail("newuser@gmail.com"))
				.thenReturn(Optional.empty());

		walletService.createWallet("newuser@gmail.com");

		verify(walletRepo).save(any(Wallet.class));
	}

	//Test wallet already exists
	@Test
	void testCreateWalletAlreadyExists() {

		Wallet wallet = Wallet.builder()
				.email("user@gmail.com")
				.balance(0.0)
				.build();

		when(walletRepo.findByEmail("user@gmail.com"))
				.thenReturn(Optional.of(wallet));

		assertThrows(
				WalletNotFoundException.class,
				() -> walletService.createWallet("user@gmail.com")
		);
	}
}