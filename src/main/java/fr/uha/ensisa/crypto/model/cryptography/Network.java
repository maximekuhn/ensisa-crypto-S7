package fr.uha.ensisa.crypto.model.cryptography;

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

import fr.uha.ensisa.crypto.model.Calendar;

/**
 * This is a Java class that represents a network and provides methods for sending and receiving data.
 * It is implemented as a singleton, which means that only one instance of the class can exist at a time. 
 * The class has a private key and a public key which are generated using RSA encryption. 
 * The public key can be accessed by calling the getPublicKey method. 
 * The class also has a method for encrypting and decrypting data using RSA encryption. 
 * It also has methods for sending a serialized Calendar object to a specified IP address, where it first sends a request to get the receiver's public RSA key, and then encrypts the calendar using the received key before sending it. 
 */
public class Network {

	/**
	 * RSA private key generated by {@link Network#Network()}.
	 * 
	 * @see Network#Network()
	 */
	private PrivateKey privateKey;

	/**
	 * RSA public key generated by {@link Network#Network()}.
	 * Accessible with {@link Network#getPublicKey()}.
	 * 
	 * @see Network#Network()
	 * @see Network#getPublicKey()
	 */
	private PublicKey publicKey;

	/**
	 * Constructor of the Network class which is called at the first
	 * {@link Network#getInstance()} call as Network is a singleton.
	 * Generate RSA {@link Network#privateKey} and {@link Network#publicKey}.
	 * 
	 * @see Network#getInstance()
	 * @see Network#instance
	 * @see Network#privateKey
	 * @see Network#publicKey
	 * @see Network#getPublicKey()
	 */
	private Network() {
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
		/*
		 * FileOutputStream file = new FileOutputStream("public.key");
		 * file.write(publicKey.getEncoded());
		 * file.close();
		 */
	}

	/**
	 * Instance of Network as Network is a singleton.
	 * Initiated at the first call of {@link Network#getInstance()}.
	 * 
	 * @see Network#getInstance()
	 */
	private static Network instance;

	/**
	 * Initiate at the first call the {@link Network#instance} calling
	 * {@link Network#Network()}.
	 * 
	 * @return Network : return the Network instance.
	 * @see Network#instance
	 * @see Network#Network()
	 */
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

	public String encryptRSA(String str, PublicKey _key)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, _key);
		return Base64.getEncoder().encodeToString(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
	}

	/**
	 * Get the public RSA key generated in {@link Network#Network()}.
	 * 
	 * @return PublicKey : return the RSA public key.
	 * @see Network#Network()
	 */
	public PublicKey getPublicKey() {
		return publicKey;
	}

	/**
	 * <p>
	 * Send a serialized {@link Calendar} to a specified IP address.
	 * The receiver needs to have the function {@link Network#receive()} executing.
	 * </p>
	 * <p>
	 * Send first a request to get the receiver public RSA key
	 * ({@link Network#publicKey}).
	 * When the key is received, encrypt the calendar with
	 * {@link Network#encryptRSA()} and send it to the receiver.
	 * </p>
	 * 
	 * @param the loaded serialized {@link Calendar} to send.
	 * @param the IP address waiting for reception of a serialized {@link Calendar}.
	 * @see Network#reciever()
	 * @see Network#publicKey
	 * @see Network#encryptRSA()
	 */
	public void sender(String calendar, String ipAddress) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeySpecException {
		// create a socket connection to the specified IP address
		Socket socket = new Socket(ipAddress, 12345);
		// socket.setSoTimeout(10000);
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
		PublicKey recieverPublicKey = KeyFactory.getInstance("RSA")
				.generatePublic(new X509EncodedKeySpec(recieverPublicKeyEncoded)); // encrypt the calendar using the
																					// received public key
		String encryptedCalendar = encryptRSA(calendar, recieverPublicKey);
		// send the encrypted calendar to the reciever
		outputStream.write(encryptedCalendar.getBytes());
		outputStream.flush();
		outputStream.close();
		inputStream.close();
		socket.close();
	}

	/**
	 * <p>
	 * Receive a serialized {@link Calendar} over a network.
	 * The sender needs execute the function {@link Network#sender()} while this
	 * function is executing.
	 * </p>
	 * <p>
	 * First receive a request for the public RSA key ({@link Network#publicKey})
	 * then send it to the sender.
	 * Then receive the encrypted the serialized {@link Calendar} and decrypt it
	 * with {@link Network#encryptRSA()}.
	 * </p>
	 * 
	 * @return String : return the serialized {@link Calendar}.
	 * @see Network#sender()
	 * @see Network#privateKey
	 * @see Network#decryptRSA()
	 */
	public String reciever() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException, IOException {
		// create a server socket to listen for incoming connections
		ServerSocket serverSocket = new ServerSocket(12345);
		// serverSocket.setSoTimeout(60000);
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
			throw new Error("Can't load");
		}
	}

}
