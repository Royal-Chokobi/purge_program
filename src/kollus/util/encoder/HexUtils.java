package kollus.util.encoder;

public final class HexUtils {

    // -------------------------------------------------------------- Constants


    /**
     *  Table for HEX to DEC byte translation.
     */
    private static final int[] DEC = {
        00, 01, 02, 03, 04, 05, 06, 07,  8,  9, -1, -1, -1, -1, -1, -1,
        -1, 10, 11, 12, 13, 14, 15, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, 10, 11, 12, 13, 14, 15,
    };


    /**
     * Table for DEC to HEX byte translation.
     */
    private static final byte[] HEX =
    { (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5',
      (byte) '6', (byte) '7', (byte) '8', (byte) '9', (byte) 'a', (byte) 'b',
      (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f' };


    /**
     * Table for byte to hex string translation.
     */
    private static final char[] hex = "0123456789abcdef".toCharArray();

    // --------------------------------------------------------- Static Methods


    public static int getDec(int index){
        // Fast for correct values, slower for incorrect ones
        try {
            return DEC[index - '0'];
        } catch (ArrayIndexOutOfBoundsException ex) {
            return -1;
        }
    }

    public static byte getHex(int index){
        return HEX[index];
    }

    public static String toHexString(byte[] bytes)
    {
        if(null == bytes) {
            return null;
        }

        StringBuilder sb = new StringBuilder(bytes.length << 1);

        for(int i=0; i<bytes.length; ++i) {
            sb.append(hex[(bytes[i] & 0xf0) >> 4])
                .append(hex[(bytes[i] & 0x0f)])
                ;
        }

        return sb.toString();
    }
}
