package by.bsu.wialontransport.protocol.newwing.decoder;

import by.bsu.wialontransport.protocol.core.decoder.packages.FixStringPrefixedBinaryPackageDecoder;
import by.bsu.wialontransport.protocol.newwing.model.packages.request.NewWingRequestPackage;
import io.netty.buffer.ByteBuf;

public abstract class NewWingPackageDecoder extends FixStringPrefixedBinaryPackageDecoder {

    public NewWingPackageDecoder(final String prefix) {
        super(prefix);
    }

    @Override
    protected final NewWingRequestPackage decodeWithoutPrefix(final ByteBuf buffer) {
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
