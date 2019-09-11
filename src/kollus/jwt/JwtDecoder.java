package kollus.jwt;

import java.security.MessageDigest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import kollus.util.encoder.Base64;
import kollus.util.string.StringUtils;

public class JwtDecoder {

	private String jwt = null;
	
	public JwtDecoder(String _jwt){
		this.jwt = _jwt;
	}
	
	private String[] splitJwt() throws Exception{
		if(jwt == null || jwt.length() <= 0){
			return null;
		}
		String[] parts = jwt.split("\\.");
		if (parts.length == 2 && jwt.endsWith(".")) {
			parts = new String[] { parts[0], parts[1], "" };
		}
		if (parts.length != 3) {
			throw new Exception(String.format("The token was expected to have 3 parts, but got %s.", parts.length));
		}
		return parts;
	}
	public String[] decodeJwt() throws Exception {
		if(jwt == null || jwt.length() <= 0){
			return null;
		}
		String[] parts = splitJwt();
		String headerJson = StringUtils.newStringUtf8(Base64.decodeBase64(parts[0]));
		String payloadJson = StringUtils.newStringUtf8(Base64.decodeBase64(parts[1]));
		String signature = parts[2];
		return new String[]{headerJson, payloadJson, signature};
	}
	public boolean verify(String secretKey) throws Exception{
		if(jwt == null || jwt.length() <= 0){
			return false;
		}
		String[] parts = splitJwt();
		byte[] contentBytes = String.format("%s.%s", parts[0], parts[1]).getBytes("UTF-8");
		byte[] signatureBytes = Base64.decodeBase64(parts[2]);
		
		final Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256"));
		byte[] newSignatureBytes = mac.doFinal(contentBytes);
		return MessageDigest.isEqual(newSignatureBytes, signatureBytes);
		
	}
	
}
