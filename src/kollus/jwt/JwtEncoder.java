package kollus.jwt;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import kollus.util.encoder.Base64;
import kollus.util.string.StringUtils;

/***
 * 
 * @author Yang Hyeon Deok
 *
 */
public class JwtEncoder {

	/***
	 * 
	 * @param headerJson
	 * @param payloadJson
	 * @param secretKey
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	private String createJwt(final String headerJson, final String payloadJson, String secretKey)
			throws NoSuchAlgorithmException, InvalidKeyException {
		String header = Base64.encodeBase64URLSafeString(StringUtils.getBytesUtf8(headerJson));
		String payload = Base64.encodeBase64URLSafeString(StringUtils.getBytesUtf8(payloadJson));
		String content = String.format("%s.%s", header, payload);
		final Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(new SecretKeySpec(StringUtils.getBytesUtf8(secretKey), "HmacSHA256"));
		byte[] signatureBytes = mac.doFinal(StringUtils.getBytesUtf8(content));
		String signature = Base64.encodeBase64URLSafeString(signatureBytes);
		return String.format("%s.%s", content, signature);
	}

	/**
	 * 
	 * 
	 * @param payloadJson
	 * @param secretKey
	 * @return
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 */
	public String createJwt(final String payloadJson, String secretKey)
			throws InvalidKeyException, NoSuchAlgorithmException {
		String headerJson = "{\"alg\": \"HS256\",\"typ\": \"JWT\"}";
		return createJwt(headerJson, payloadJson, secretKey);
	}

	/***
	 * 
	 * @param cuid
	 * @param exptMinutes
	 * @param secretKey
	 * @param mediaKeys
	 * @return
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 */
	public String createJwt(String cuid, int exptMinutes, String secretKey, String... mediaKeys)
			throws InvalidKeyException, NoSuchAlgorithmException {
		String fmt_payloadJson = "{\"cuid\": \"%s\",\"expt\": %d,\"mc\": [%s]}";
		StringBuilder sb = new StringBuilder();
		int nMediakeys = mediaKeys.length;
		for (int idx = 0; idx < nMediakeys; idx++) {
			sb.append("{\"mckey\":\"");
			sb.append(mediaKeys[idx]);
			sb.append("\"}");
			if (idx < nMediakeys - 1) {
				sb.append(",");
			}
		}

		String listMeidaKey = sb.toString();
		Date now = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(now);
		c.add(Calendar.MINUTE, exptMinutes);
		long expt = c.getTime().getTime();
		final String payloadJson = String.format(fmt_payloadJson, cuid, expt, listMeidaKey);
		return createJwt(payloadJson, secretKey);

	}

	/***
	 * 
	 * @param cuid
	 * @param exptMinutes
	 * @param secretKey
	 * @param jms
	 * @return
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 */
	public String createJwt(String cuid, int exptMinutes, String secretKey, MediaItem... jms)
			throws InvalidKeyException, NoSuchAlgorithmException {
		String fmt_payloadJson = "{\"cuid\": \"%s\",\"expt\": %d,\"mc\": [%s]}";
		StringBuilder sb = new StringBuilder();
		int njms = jms.length;
		for (int idx = 0; idx < njms; idx++) {
			sb.append(jms[idx].toJson());
			if (idx < njms - 1) {
				sb.append(",");
			}
		}

		String listMeidaKey = sb.toString();
		Date now = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(now);
		c.add(Calendar.MINUTE, exptMinutes);
		long expt = c.getTime().getTime();
		final String payloadJson = String.format(fmt_payloadJson, cuid, expt, listMeidaKey);
		return createJwt(payloadJson, secretKey);

	}

	/***
	 * 
	 * @param host
	 * @param userKey
	 * @param cuid
	 * @param secretKey
	 * @param exptMinutes
	 * @param mediaKeys
	 * @return
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 */
	public String getPlayUrl(String host, String userKey, String cuid, String secretKey, int exptMinutes,
			String... mediaKeys) throws InvalidKeyException, NoSuchAlgorithmException {

		String token = createJwt(cuid, exptMinutes, secretKey, mediaKeys);
		return String.format("%s/s?jwt=%s&custom_key=%s", host, token, userKey);
	}

	/***
	 * 
	 * @param host
	 * @param userKey
	 * @param cuid
	 * @param secretKey
	 * @param exptMinutes
	 * @param jms
	 * @return
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 */
	public String getPlayUrl(String host, String userKey, String cuid, String secretKey, int exptMinutes,
			MediaItem... jms) throws InvalidKeyException, NoSuchAlgorithmException {

		String token = createJwt(cuid, exptMinutes, secretKey, jms);
		return String.format("%s/s?jwt=%s&custom_key=%s", host, token, userKey);
	}
	
	public String getPlayUrl(String host, String token, String userKey){
		return String.format("%s/s?jwt=%s&custom_key=%s", host, token, userKey);
	}
	
	public String getSrUrl(String host, String token, String userKey){
		return String.format("%s/sr?jwt=%s&custom_key=%s&download", host, token, userKey);
	}
}
