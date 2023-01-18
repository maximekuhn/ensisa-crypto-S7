package fr.uha.ensisa.crypto.model.cryptography;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.CountDownLatch;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NetworkTest {
	private Network sut; // System Under Test

	@BeforeEach
	public void init() throws NoSuchAlgorithmException, IOException {
		sut = Network.getInstance();
	}

	@Test
	public void RSATest() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException {
		String str = "test";
		String encrypted = sut.encryptRSA(str, sut.getPublicKey());
		String decrypted = sut.decryptRSA(encrypted);
		assertNotNull(encrypted);
		assertEquals(str, decrypted);
	}

	@Test
	public void Network_Test() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException, InterruptedException {
		String calendar = "Meeting at 3pm";
		String ipAddress = "127.0.0.1";
		CountDownLatch latch = new CountDownLatch(1);
		// start a new thread for the sender method
		Thread senderThread = new Thread(() -> {
			try {
				sut.sender(calendar, ipAddress);
			} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
					| BadPaddingException | IOException | InvalidKeySpecException e) {
				e.printStackTrace();
			}
		});

		// start a new thread for the receiver method
		Thread receiverThread = new Thread(() -> {
			try {
				String receivedCalendar = sut.receiver();
				assertEquals(calendar, receivedCalendar);
				latch.countDown();
			} catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
					| IllegalBlockSizeException | BadPaddingException e) {
				e.printStackTrace();
			}
		});
		receiverThread.start();
		Thread.sleep(100);
		senderThread.start();
		latch.await();
	}
}