package fr.uha.ensisa.crypto;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import fr.uha.ensisa.crypto.model.Agenda;

public class Network {
	private PrivateKey privateKey;
	private PublicKey publicKey;

	private Network(){
		KeyPairGenerator kpg;
		try {
			kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(2048);
			KeyPair caPk = kpg.generateKeyPair();
			privateKey = caPk.getPrivate();
			publicKey = caPk.getPublic();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		/*FileOutputStream file = new FileOutputStream("public.key");
		file.write(publicKey.getEncoded());
		file.close();*/
	}
	
	private static Network instance;

	public static Network getInstance() {
		if (instance == null) {
			instance = new Network();
		}
		return instance;
	}

	public String decryptRSA(String encrypted) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return new String(cipher.doFinal(Base64.getDecoder().decode(encrypted)), StandardCharsets.UTF_8);
	}

	public String encryptRSA(String str, PublicKey _key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, _key);
		return Base64.getEncoder().encodeToString(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public void sender(String calendar, String ipAddress) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeySpecException {
	    // create a socket connection to the specified IP address
	    Socket socket = new Socket(ipAddress, 12345);
	    // create a OutputStream to send the public key request
	    OutputStream outputStream = socket.getOutputStream();
	    // write the request message to the OutputStream
	    outputStream.write("Public Key Request".getBytes());
	    outputStream.flush();
	    // create an InputStream to read the public key response
	    InputStream inputStream = socket.getInputStream();
	    // read the public key response into a byte array
	    byte[] buffer = new byte[4096];
	    int bytesRead = inputStream.read(buffer);
	    // convert the byte array to a PublicKey object
	    byte[] recieverPublicKeyEncoded = Arrays.copyOfRange(buffer, 0, bytesRead);
	    PublicKey recieverPublicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(recieverPublicKeyEncoded));	    // encrypt the calendar using the received public key
	    String encryptedCalendar = encryptRSA(calendar,recieverPublicKey);
	    // send the encrypted calendar to the reciever
	    outputStream.write(encryptedCalendar.getBytes());
	    outputStream.flush();
	    outputStream.close();
	    inputStream.close();
	    socket.close();
	}

	public String reciever() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
	    // create a server socket to listen for incoming connections
	    ServerSocket serverSocket = new ServerSocket(12345);
	    // accept the incoming connection
	    Socket socket = serverSocket.accept();
	    // create an InputStream to read the incoming data
	    InputStream inputStream = socket.getInputStream();
	    // read the incoming data into a byte array
	    byte[] buffer = new byte[4096];
	    int bytesRead = inputStream.read(buffer);
	    // convert the byte array to a string
	    String request = new String(buffer, 0, bytesRead);
	    // check if the request is for a public key
	    if (request.equals("Public Key Request")) {
	        // create a OutputStream to send the public key
	        OutputStream outputStream = socket.getOutputStream();
	        // write the public key to the OutputStream
	        outputStream.write(publicKey.getEncoded());
	        outputStream.flush();
	        // read the encrypted calendar from the InputStream
	        bytesRead = inputStream.read(buffer);
	        // decrypt the calendar using the private key
	        String calendar = decryptRSA(new String(buffer, 0, bytesRead));
	        outputStream.close();
	        inputStream.close();
	        socket.close();
	        serverSocket.close();
	        return calendar;
	    } else {
	        // if the request is not for a public key, return an error message
	    	serverSocket.close();
	        return "Error: Invalid request";
	    }
	}

}
