package kollus.util.encoder;

import kollus.util.exception.EncoderException;

public interface Encoder {
	Object encode(Object source) throws EncoderException;
}
