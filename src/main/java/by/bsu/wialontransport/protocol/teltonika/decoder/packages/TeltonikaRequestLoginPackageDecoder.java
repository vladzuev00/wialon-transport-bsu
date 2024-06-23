package by.bsu.wialontransport.protocol.teltonika.decoder.packages;

import by.bsu.wialontransport.protocol.core.decoder.packages.PackageBufferDecoder;
import by.bsu.wialontransport.protocol.core.model.packages.Package;
import io.netty.buffer.ByteBuf;

public class TeltonikaRequestLoginPackageDecoder extends PackageBufferDecoder<Short> {
    public TeltonikaRequestLoginPackageDecoder(Short aShort) {
        super(aShort);
    }

    @Override
    public Package decode(ByteBuf byteBuf) {
        return null;
    }
}
