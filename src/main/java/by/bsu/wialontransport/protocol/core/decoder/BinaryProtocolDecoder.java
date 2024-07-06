package by.bsu.wialontransport.protocol.core.decoder;

import by.bsu.wialontransport.protocol.core.decoder.packages.PackageDecoder;
import io.netty.buffer.ByteBuf;

import java.util.List;

public final class BinaryProtocolDecoder extends ProtocolDecoder<ByteBuf> {

    public BinaryProtocolDecoder(final List<? extends PackageDecoder<ByteBuf>> packageDecoders) {
        super(packageDecoders);
    }

    @Override
    protected ByteBuf createSource(final ByteBuf buffer) {
        return buffer;
    }
}
