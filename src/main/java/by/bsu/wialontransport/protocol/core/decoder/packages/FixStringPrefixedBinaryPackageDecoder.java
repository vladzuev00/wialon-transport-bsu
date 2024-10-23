package by.bsu.wialontransport.protocol.core.decoder.packages;

import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

import static java.nio.charset.StandardCharsets.US_ASCII;

public abstract class FixStringPrefixedBinaryPackageDecoder extends PrefixedBinaryPackageDecoder<String> {
    static final Charset CHARSET = US_ASCII;

    public FixStringPrefixedBinaryPackageDecoder(final String prefix) {
        super(prefix);
    }

//    @Override
//    protected final int getPrefixByteCount() {
//        return "getPrefix()".getBytes(CHARSET).length;
//    }
//
//    @Override
//    protected final String createPrefix(final ByteBuf prefixBytes) {
//        return prefixBytes.toString(CHARSET);
//    }
}
