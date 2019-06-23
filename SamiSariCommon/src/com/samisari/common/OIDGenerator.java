package com.samisari.common;

import java.util.Random;

public class OIDGenerator {
	public static final char[]	OID_CHARS		= new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
			'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'x',
			'w', 'v', 'y', 'z'					};

	public static String getRandomString(int length) {
		StringBuilder sb = new StringBuilder();
		Random rndm = new Random();
		String oidAlphabet = new String(OID_CHARS);
		for (int i = 0; i < length; i++) {
			int rnd = rndm.nextInt(oidAlphabet.length());
			sb.append(oidAlphabet.charAt(rnd));
		}
		return sb.toString();
	}

	public static String getRandomString(int length, String alphabet) {
		StringBuilder sb = new StringBuilder();
		Random rndm = new Random();
		for (int i = 0; i < length; i++) {
			int rnd = rndm.nextInt(alphabet.length());
			sb.append(alphabet.charAt(rnd));
		}
		return sb.toString();
	}
}
