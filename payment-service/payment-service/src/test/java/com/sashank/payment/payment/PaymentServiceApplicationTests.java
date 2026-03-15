package com.sashank.payment.payment;

import com.sashank.payment.payment.client.NotificationClient;
import com.sashank.payment.payment.client.WalletClient;
import com.sashank.payment.payment.dto.PaymentRequest;
import com.sashank.payment.payment.entity.Transaction;
import com.sashank.payment.payment.exception.PaymentFailedException;
import com.sashank.payment.payment.repo.TransactionRepo;
import com.sashank.payment.payment.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

	@Mock
	private TransactionRepo transactionRepo;

	@Mock
	private WalletClient walletClient;

	@Mock
	private NotificationClient notificationClient;

	@InjectMocks
	private PaymentService paymentService;

	// Test successful payment
	@Test
	void testPaymentSuccess() {

		PaymentRequest request = new PaymentRequest();
		request.setReceiverMail("receiver@gmail.com");
		request.setAmount(500.0);

		Transaction transaction = Transaction.builder()
				.Id(1L)
				.senderEmail("sender@gmail.com")
				.receiverEmail("receiver@gmail.com")
				.amount(500.0)
				.status("SUCCESS")
				.build();

		when(transactionRepo.save(any(Transaction.class))).thenReturn(transaction);

		Transaction result = paymentService.pay("sender@gmail.com", request);

		assertEquals("SUCCESS", result.getStatus());
		assertEquals("sender@gmail.com", result.getSenderEmail());
		assertEquals("receiver@gmail.com", result.getReceiverEmail());

		verify(walletClient).debit(any());
		verify(walletClient).credit(any());
		verify(notificationClient).sendNotification(any());
	}

	// Test sender and receiver same
	@Test
	void testSenderReceiverSame() {

		PaymentRequest request = new PaymentRequest();
		request.setReceiverMail("same@gmail.com");
		request.setAmount(500.0);

		assertThrows(
				PaymentFailedException.class,
				() -> paymentService.pay("same@gmail.com", request)
		);
	}

	// Test invalid amount
	@Test
	void testInvalidAmount() {

		PaymentRequest request = new PaymentRequest();
		request.setReceiverMail("receiver@gmail.com");
		request.setAmount(0.0);

		assertThrows(
				PaymentFailedException.class,
				() -> paymentService.pay("sender@gmail.com", request)
		);
	}

	// Test wallet debit failure
	@Test
	void testDebitFailure() {

		PaymentRequest request = new PaymentRequest();
		request.setReceiverMail("receiver@gmail.com");
		request.setAmount(500.0);

		doThrow(new RuntimeException("Debit failed"))
				.when(walletClient)
				.debit(any());

		assertThrows(
				PaymentFailedException.class,
				() -> paymentService.pay("sender@gmail.com", request)
		);

		verify(walletClient).debit(any());
	}

	// Test transaction history
	@Test
	void testGetHistory() {

		paymentService.getHistory("user@gmail.com");

		verify(transactionRepo)
				.findBySenderEmailOrReceiverEmail("user@gmail.com", "user@gmail.com");
	}
}