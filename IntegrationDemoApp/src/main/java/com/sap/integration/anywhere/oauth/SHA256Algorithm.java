package com.sap.integration.anywhere.oauth;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

public class SHA256Algorithm {

	public static String encode(String clientSecret, String raw) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
		Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
		SecretKeySpec secret_key = new SecretKeySpec( clientSecret.getBytes("UTF-8"), "HmacSHA256" );;
		sha256_HMAC.init(secret_key);

		String hash = Hex.encodeHexString(sha256_HMAC.doFinal(raw.getBytes("UTF-8")));
		return hash;
	}
}
