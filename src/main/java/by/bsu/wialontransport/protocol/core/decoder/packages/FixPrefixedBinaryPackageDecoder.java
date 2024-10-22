package by.bsu.wialontransport.protocol.core.decoder.packages;

import io.netty.buffer.ByteBuf;

public abstract class FixPrefixedBinaryPackageDecoder<PREFIX> extends PrefixedPackageDecoder<ByteBuf, PREFIX> {

    public FixPrefixedBinaryPackageDecoder(final PREFIX prefix) {
        super(prefix);
    }

//    @Override
//    protected final PREFIX readPrefix(final ByteBuf buffer) {
//        final ByteBuf bytes = buffer.slice(0, getPrefixByteCount());
//        bytes.retain();
//        try {
//            return createPrefix(bytes);
//        } finally {
//            bytes.release();
//        }
//    }
//
//    @Override
//    protected final ByteBuf skip(final ByteBuf buffer, int length) {
//        return buffer.skipBytes(getPrefixByteCount());
//    }

    protected abstract int getPrefixByteCount();

    protected abstract PREFIX createPrefix(final ByteBuf prefixBytes);

    @Override
    protected PREFIX readPrefix(ByteBuf buf) {
        return null;
    }

    @Override
    protected int getLength(PREFIX prefix) {
        return 0;
    }

    @Override
    protected ByteBuf skip(ByteBuf buf, int length) {
        return null;
    }

    @Override
    protected Object decodeInternal(ByteBuf buf) {
        return null;
    }
}
