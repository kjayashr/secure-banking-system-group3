package com.ss.security;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

public class PKICertificate {
	
	private static Cipher cipher;
	private static String algorithm = "RSA";
	private static String charset = "UTF-8";
	
	private static PrivateKey getPrivateKey(String keyString) throws Exception {
		byte[] keyBytes = Base64.decodeBase64(keyString);
	    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
	    KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
	    return keyFactory.generatePrivate(keySpec);
	}
	
	private static PublicKey getPublicKey(String keyString) throws Exception {
		byte[] keyBytes = Base64.decodeBase64(keyString);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
		return keyFactory.generatePublic(keySpec);
	}
	
	public static String lock(String data, String publicKey) throws Exception {
		cipher = Cipher.getInstance("RSA");
		PublicKey key = getPublicKey(publicKey);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return Base64.encodeBase64String(cipher.doFinal(data.getBytes(charset)));
	}
	
	public static String unlock(String data, String privateKey) throws Exception {
		cipher = Cipher.getInstance("RSA");
		PrivateKey key = getPrivateKey(privateKey);
		cipher.init(Cipher.DECRYPT_MODE, key);
		return new String(cipher.doFinal(Base64.decodeBase64(data)), charset);
	}
}
