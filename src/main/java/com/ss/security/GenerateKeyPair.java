package com.ss.security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import org.apache.commons.codec.binary.Base64;

public class GenerateKeyPair {
	private KeyPairGenerator KPGen;
	private KeyPair pair;
	private PrivateKey kPrivate;
	private PublicKey kPublic;
	private String algorithm = "RSA";
	private int keyLength = 1024;
	
	public GenerateKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException {
        this.KPGen = KeyPairGenerator.getInstance(algorithm);
        this.KPGen.initialize(keyLength, new SecureRandom());
        
        this.pair = this.KPGen.generateKeyPair();
		this.kPrivate = pair.getPrivate();
		this.kPublic = pair.getPublic();
    }

	public String getPrivateKey() {
		return Base64.encodeBase64String(this.kPrivate.getEncoded());
	}

	public String getPublicKey() {
		return Base64.encodeBase64String(this.kPublic.getEncoded());
	}
}
