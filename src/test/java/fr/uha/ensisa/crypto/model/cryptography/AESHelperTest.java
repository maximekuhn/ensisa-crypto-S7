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

public class AESHelperTest {

    private AESHelper sut; // System Under Test

    @Test
    @DisplayName("Test encryption / decryption")
    void testHelper() {
        String data = "this is some plain text";
        String password = "password1234";
        byte[] iv = null;
        byte[] salt = null;

        this.sut = new AESHelper(data, password, iv, salt);

        String encrypted;
        try {
            encrypted = this.sut.encryptAES();
            assertNotEquals(data, encrypted, "data should have been encrypted");

            this.sut.setData(encrypted);

            String decrypted = this.sut.decryptAES();
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
        this.sut = new AESHelper("data", "password", null, new byte[64]);
        assertThrows(IllegalStateException.class, () -> this.sut.decryptAES());
    }

    @Test
    @DisplayName("Test null salt")
    void testNullSalt() {
        this.sut = new AESHelper("data", "password", new byte[16], null);
        assertThrows(IllegalStateException.class, () -> this.sut.decryptAES());
    }

    @Test
    @DisplayName("Test bad password")
    void testBadPassword() {
        String data = "this is some plain text";
        String password = "password1234";
        String badPassword = "badPassword1234";
        byte[] iv = null;
        byte[] salt = null;

        this.sut = new AESHelper(data, password, iv, salt);

        try {
            String encrypted = this.sut.encryptAES();
            this.sut.setData(encrypted);
            assertNotEquals(data, encrypted);

            this.sut.setPassword(badPassword);

            assertThrows(Exception.class, () -> this.sut.decryptAES());

            // test with a password that has same length as original password
            String badPassword2 = "password1233";
            this.sut.setPassword(badPassword2);
            assertThrows(Exception.class, () -> this.sut.decryptAES());

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

        this.sut = new AESHelper(data, password, iv, salt);

        try {
            // get IV
            String encrypted = this.sut.encryptAES();
            byte[] oldIV = this.sut.getIV();
            assertNotNull(oldIV);
            this.sut.setData(encrypted);

            // check that decryption still works
            String decrypted = this.sut.decryptAES();
            assertEquals(data, decrypted, "data should have been decrypted");

            // set new IV and encrypt data with it
            this.sut.setIV(null);
            this.sut.setData(data);
            encrypted = this.sut.encryptAES(); // to generate a new IV
            byte[] newIV = this.sut.getIV();
            assertNotNull(newIV);

            assertNotEquals(newIV, oldIV);
            assertFalse(Arrays.equals(oldIV, newIV));

            // check that new IV can decrypt data
            // sut.setIV(newIV);
            this.sut.setData(encrypted);
            decrypted = this.sut.decryptAES();
            assertEquals(data, decrypted, "data should have been decrypted");

            // check that old IV can't decrypt data
            // sut.setData(encrypted);
            this.sut.setIV(oldIV);
            decrypted = this.sut.decryptAES();
            assertNotEquals(data, decrypted, "data should not have been properly decrypted");

        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeySpecException
                | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            fail("Should not been here");
            e.printStackTrace();
        }

    }
}
