package fr.uha.cryptography;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import fr.uha.ensisa.crypto.model.cryptography.AESHelper;

public class AESHelperTest {
    
    private AESHelper sut; // System Under Test

    @Test
    @DisplayName("Test encryption / decryption")
    void testHelper() {
        String data = "this is some plain text";
        String password = "password1234";
        byte[] iv = null;

        sut = new AESHelper(data, password, iv);
        
        String encrypted;
        try {
            encrypted = sut.encryptAES();
            assertNotEquals(data, encrypted, "data should have been encrypted");

            sut.setData(encrypted);

            String decrypted = sut.decryptAES();
            assertEquals(data, decrypted, "decrypted data should be equal to data");

        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeySpecException
                | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            fail("General case should not throw any exception");
            e.printStackTrace();
        }
        
    }

    @Test
    @DisplayName("Test null iv")
    void testNullIv() {
        sut = new AESHelper("data", "password", null);
        assertThrows(IllegalStateException.class, () -> sut.decryptAES());
    }
}
