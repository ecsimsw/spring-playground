
package com.ecsimsw.apievent.support;

import org.apache.pulsar.shade.org.apache.commons.codec.binary.Base64;
import org.apache.pulsar.shade.org.apache.commons.codec.binary.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class AESBase64Decrypt {

	private static final Logger logger = LoggerFactory.getLogger(AESBase64Decrypt.class);

	public static String decrypt(String data, String secretKey) {
		try {
			Key key = new SecretKeySpec(secretKey.getBytes(), "AES");
			Cipher c = Cipher.getInstance("AES");
			c.init(Cipher.DECRYPT_MODE, key);
			byte[] decodedValue = Base64.decodeBase64(data);
			byte[] decValue = c.doFinal(decodedValue);
			return StringUtils.newStringUtf8(decValue);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			logger.error("failed to decrypt data", e);
			throw new IllegalArgumentException("failed to decrypt data", e);
		}
	}
}
