package com.example.resistance.common.utils;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.springframework.util.StringUtils;

public class CipherUtil {

	public static String encrypt(String target) {

		try {
			if (StringUtils.isEmpty(target)) {
				return null;
			}
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(target.getBytes());
			return Base64.getEncoder().encodeToString(md.digest());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

}