package com.ss.service;

import org.apache.commons.codec.binary.Base64;

public class EncryptDecryptUtil {
	
	private static String encryption(String originalData, int cryptCount) {
		String encryptedData = originalData;
		
		for(int i = 1; i <= cryptCount; i++) {
			encryptedData = Base64.encodeBase64String(encryptedData.getBytes());
		}
		return encryptedData;
	}
	
	private static String decryption(String encryptedData, int cryptCount) {
		String originalData = encryptedData;
		
		for(int i = 1; i <= cryptCount; i++) {
			originalData = new String(Base64.decodeBase64(originalData));
		}
		return originalData;
	}
	
	
	public static String singleEncryption(String originalData) {
		return encryption(originalData, 1);
	}
	
	public static String singleDecryption(String encryptedData) {
		return decryption(encryptedData, 1);
	}
	
	public static String doubleEncryption(String originalData) {
		return encryption(originalData, 1);
	}
	
	public static String doubleDecryption(String encryptedData) {
		return decryption(encryptedData, 1);
	}
	
	public static String trippleEncryption(String originalData) {
		return encryption(originalData, 1);
	}
	
	public static String trippleDecryption(String encryptedData) {
		return decryption(encryptedData, 1);
	}
	
	
	
}
