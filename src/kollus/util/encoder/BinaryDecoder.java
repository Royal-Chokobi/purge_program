package kollus.util.encoder;

import kollus.util.exception.EncoderException;

public interface BinaryDecoder extends Decoder{
	byte[] decode(byte[] source) throws EncoderException;
}
