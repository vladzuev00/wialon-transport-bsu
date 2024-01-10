package by.bsu.wialontransport.protocol.newwing.decoder.packages;

import by.bsu.wialontransport.protocol.core.decoder.packages.PackageBufferDecoder;
import by.bsu.wialontransport.protocol.newwing.model.packages.request.NewWingRequestPackage;
import by.bsu.wialontransport.protocol.newwing.model.packages.request.builder.NewWingRequestPackageBuilder;
import io.netty.buffer.ByteBuf;

public abstract class NewWingPackageDecoder<
        PACKAGE extends NewWingRequestPackage,
        BUILDER extends NewWingRequestPackageBuilder<PACKAGE>
        >
        extends PackageBufferDecoder<String, PACKAGE> {

    public NewWingPackageDecoder(final String packagePrefix) {
        super(packagePrefix);
    }

    @Override
    public final PACKAGE decode(final ByteBuf buffer) {
        final BUILDER packageBuilder = createPackageBuilder();
        decodeUntilChecksum(buffer, packageBuilder);
        decodeChecksum(buffer, packageBuilder);
        return packageBuilder.build();
    }

    protected abstract BUILDER createPackageBuilder();

    protected abstract void decodeUntilChecksum(final ByteBuf buffer, final BUILDER packageBuilder);

    private void decodeChecksum(final ByteBuf buffer, final BUILDER packageBuilder) {
        final int checksum = buffer.readIntLE();
        packageBuilder.setChecksum(checksum);
    }
}
