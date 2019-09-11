package kollus.util.encoder;

import kollus.util.exception.EncoderException;

public interface BinaryEncoder extends Encoder{
	byte[] encode(byte[] source) throws EncoderException;
}
