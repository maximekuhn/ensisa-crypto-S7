package fr.uha.ensisa.crypto.model.cryptography;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

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

public class RC5HelperTest {

    private RC5Helper sut; // System Under Test

    @Test
    @DisplayName("Test encryption / decryption")
    void testRC5Helper() {
        String data = "this is some plain text";
        String password = "password1234";
        byte[] iv = null;
        byte[] salt = null;

        sut = new RC5Helper(data, password, iv, salt);

        String encrypted;
        try {
            encrypted = sut.encryptRC5();
            assertNotEquals(data, encrypted, "data should have been encrypted");

            sut.setData(encrypted);

            String decrypted = sut.decryptRC5();
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
        sut = new RC5Helper("data", "password", null, null);
        assertThrows(IllegalStateException.class, () -> sut.decryptRC5());
    }

    @Test
    @DisplayName("Test bad password")
    void testBadPassword() {
        String data = "this is some plain text";
        String password = "password1234";
        String badPassword = "badPassword1234";
        byte[] iv = null;
        byte[] salt = null;

        sut = new RC5Helper(data, password, iv, salt);

        try {
            String encrypted = sut.encryptRC5();
            sut.setData(encrypted);
            assertNotEquals(data, encrypted);

            sut.setPassword(badPassword);

            assertThrows(Exception.class, () -> sut.decryptRC5());

            // test with a password that has same length as original password
            String badPassword2 = "password1233";
            sut.setPassword(badPassword2);
            assertThrows(Exception.class, () -> sut.decryptRC5());

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

        sut = new RC5Helper(data, password, iv, salt);

        try {
            // get IV
            String encrypted = sut.encryptRC5();
            byte[] oldIV = sut.getIV();
            assertNotNull(oldIV);
            sut.setData(encrypted);

            // check that decryption still works
            String decrypted = sut.decryptRC5();
            assertEquals(data, decrypted, "data should have been decrypted");

            // set new IV and encrypt data with it
            sut.setIV(null);
            sut.setData(data);
            encrypted = sut.encryptRC5(); // to generate a new IV
            byte[] newIV = sut.getIV();
            assertNotNull(newIV);

            assertNotEquals(newIV, oldIV);
            assertFalse(Arrays.equals(oldIV, newIV));

            // check that new IV can decrypt data
            // sut.setIV(newIV);
            sut.setData(encrypted);
            decrypted = sut.decryptRC5();
            assertEquals(data, decrypted, "data should have been decrypted");

            // check that old IV can't decrypt data
            // sut.setData(encrypted);
            sut.setIV(oldIV);
            decrypted = sut.decryptRC5();
            assertNotEquals(data, decrypted, "data should not have been properly decrypted");

        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeySpecException
                | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            fail("Should not been here");
            e.printStackTrace();
        }
    }
    
    @Test
    @DisplayName("Test null salt")
    void testNullSalt() {
        sut = new RC5Helper("data", "password", new byte[16], null);
        assertThrows(IllegalStateException.class, () -> sut.decryptRC5());
    }
}
