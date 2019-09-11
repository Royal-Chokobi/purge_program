package kollus.util.encoder;

import kollus.util.exception.DecoderException;

public interface Decoder {
	Object decode(Object source) throws DecoderException;
}
