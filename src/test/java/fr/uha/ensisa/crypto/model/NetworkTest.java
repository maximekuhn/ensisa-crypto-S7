package fr.uha.ensisa.crypto.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.uha.ensisa.crypto.Network;

public class NetworkTest {
	private Network sut; // System Under Test
	
	@BeforeEach
	public void init() throws NoSuchAlgorithmException, IOException {
		sut = new Network();
	}
	
	@Test
	public void RSATest() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		String str = "test";
		System.out.println(str);
		String encrypted = sut.encryptRSA(str);
		String decrypted = sut.decryptRSA(encrypted);
		System.out.println(encrypted);
		assertNotNull(encrypted);
		System.out.println(decrypted);
		assertEquals(str, decrypted);
		
	}
}