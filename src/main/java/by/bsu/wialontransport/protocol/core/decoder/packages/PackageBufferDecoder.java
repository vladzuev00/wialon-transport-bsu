package by.bsu.wialontransport.protocol.core.decoder.packages;

import io.netty.buffer.ByteBuf;

public abstract class PackageBufferDecoder<PREFIX> extends PackageDecoder<PREFIX, ByteBuf> {

    public PackageBufferDecoder(final PREFIX prefix) {
        super(prefix);
    }
}
