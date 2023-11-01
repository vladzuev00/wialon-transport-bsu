package by.bsu.wialontransport.protocol.newwing.decoder;

import by.bsu.wialontransport.protocol.core.decoder.ProtocolBufferDecoder;
import by.bsu.wialontransport.protocol.newwing.decoder.packages.NewWingPackageDecoder;
import by.bsu.wialontransport.protocol.newwing.model.packages.NewWingPackage;
import io.netty.buffer.ByteBuf;

import java.util.List;

public final class NewWingProtocolDecoder extends ProtocolBufferDecoder<String, NewWingPackage, NewWingPackageDecoder<?, ?>> {
    public NewWingProtocolDecoder(List<NewWingPackage> packageDecoders) {
        super(packageDecoders);
    }

    @Override
    protected String extractPackagePrefix(ByteBuf byteBuf) {
        return null;
    }
}
