package fr.uha.ensisa.crypto.model.cryptography;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AESHelperTest {
    
    private AESHelper sut; // System Under Test

    @Test
    @DisplayName("Test encryption / decryption")
    void testHelper() {
        String data = "this is some plain text";
        String password = "password1234";
        byte[] iv = null;
        byte[] salt = null;

        sut = new AESHelper(data, password, iv, salt);
        
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
        sut = new AESHelper("data", "password", null, null);
        assertThrows(IllegalStateException.class, () -> sut.decryptAES());
    }

    @Test
    @DisplayName("Test bad password")
    void testBadPassword() {
        String data = "this is some plain text";
        String password = "password1234";
        String badPassword ="badPassword1234";
        byte[] iv = null;
        byte[] salt = null;

        sut = new AESHelper(data, password, iv, salt);

        try {
            String encrypted = sut.encryptAES();
            sut.setData(encrypted);
            assertNotEquals(data, encrypted);

            sut.setPassword(badPassword);

            assertThrows(Exception.class, () -> sut.decryptAES());

            // test with a password that has same length as original password
            String badPassword2 = "password1233";
            sut.setPassword(badPassword2);
            assertThrows(Exception.class, () -> sut.decryptAES());

        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeySpecException
                | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            fail("Exception should have been catched earlier");
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Test modifying iv")
    void testModifyIV() {
        String data = "this is some plain text";
        String password = "password1234";
        byte[] iv = null;
        byte[] salt = null;

        sut = new AESHelper(data, password, iv, salt);

        try {
            // get IV
            String encrypted = sut.encryptAES();
            byte[] oldIV = sut.getIV();
            assertNotNull(oldIV);
            sut.setData(encrypted);

            // check that decryption still works
            String decrypted = sut.decryptAES();
            assertEquals(data, decrypted, "data should have been decrypted");

            // set new IV and encrypt data with it
            sut.setIV(null);
            sut.setData(data);
            encrypted = sut.encryptAES(); // to generate a new IV
            byte[] newIV = sut.getIV();
            assertNotNull(newIV);

            assertNotEquals(newIV, oldIV);
            assertFalse(Arrays.equals(oldIV, newIV));

            // check that new IV can decrypt data
            //sut.setIV(newIV);
            sut.setData(encrypted);
            decrypted = sut.decryptAES();
            assertEquals(data, decrypted, "data should have been decrypted");

            // check that old IV can't decrypt data
            //sut.setData(encrypted);
            sut.setIV(oldIV);
            decrypted = sut.decryptAES();
            assertNotEquals(data, decrypted, "data should not have been properly decrypted");

        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeySpecException
                | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            fail("Should not been here");
            e.printStackTrace();
        }

    }
}
