package by.bsu.wialontransport.protocol.newwing.decoder.packages;

import by.bsu.wialontransport.protocol.core.decoder.packages.FixPrefixedPackageDecoder;
import by.bsu.wialontransport.protocol.core.decoder.packages.FixStringPrefixedPackageDecoder;
import by.bsu.wialontransport.protocol.core.decoder.packages.PackageDecoderByFixedPrefix;
import by.bsu.wialontransport.protocol.newwing.model.packages.request.NewWingRequestPackage;
import io.netty.buffer.ByteBuf;

public abstract class NewWingPackageDecoder extends FixStringPrefixedPackageDecoder {

    public NewWingPackageDecoder(final String prefix) {
        super(prefix);
    }

    @Override
    public final NewWingRequestPackage decode(final ByteBuf buffer) {
        final PackageFactory factory = decodeUntilChecksum(buffer);
        final int checksum = buffer.readIntLE();
        return factory.create(checksum);
    }

    protected abstract PackageFactory decodeUntilChecksum(final ByteBuf buffer);

    @FunctionalInterface
    protected interface PackageFactory {
        NewWingRequestPackage create(final int checksum);
    }
}
