package by.bsu.wialontransport.protocol.core.decoder;

import by.bsu.wialontransport.protocol.core.decoder.packages.PackageBufferDecoder;
import by.bsu.wialontransport.protocol.protocolpackage.Package;
import io.netty.buffer.ByteBuf;

import java.util.List;

public abstract class ProtocolBufferDecoder<
        PREFIX,
        PACKAGE extends Package,
        DECODER extends PackageBufferDecoder<PREFIX, ? extends PACKAGE>
        >
        extends ProtocolDecoder<PREFIX, ByteBuf, PACKAGE, DECODER> {

    public ProtocolBufferDecoder(final List<DECODER> packageDecoders) {
        super(packageDecoders);
    }

    @Override
    public final ByteBuf createSource(final ByteBuf byteBuf) {
        return byteBuf;
    }
}
