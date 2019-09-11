package kollus.util.string;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
//import java.nio.charset.StandardCharsets;

public class StringUtils {
	 
    private static byte[] getBytes(final String string) {
    	byte[] result = null;
        if (string == null) {
            return result;
        }
        try {
			result = string.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return result;
    }

    
    public static byte[] getBytesUtf8(final String string) {
        return getBytes(string);
    }

    
    private static String newString(final byte[] bytes, String charset) {
    	String result = null;
    	if(bytes == null){
    		return null;
    	}
        try {
			result =  new String(bytes, charset);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return result;
    }

    
    public static String newStringUtf8(final byte[] bytes) {
        return newString(bytes, "UTF-8");
    }
}
