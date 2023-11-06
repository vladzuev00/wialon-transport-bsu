package by.bsu.wialontransport.protocol.newwing.decoder.packages;

import by.bsu.wialontransport.protocol.core.decoder.packages.PackageBufferDecoder;
import by.bsu.wialontransport.protocol.newwing.model.packages.request.NewWingRequestPackage;
import by.bsu.wialontransport.protocol.newwing.model.packages.request.builder.NewWingRequestPackageBuilder;
import io.netty.buffer.ByteBuf;

import java.util.function.Supplier;

public abstract class NewWingPackageDecoder<
        PACKAGE extends NewWingRequestPackage,
        BUILDER extends NewWingRequestPackageBuilder<PACKAGE>
        >
        extends PackageBufferDecoder<String, PACKAGE> {
    private final Supplier<BUILDER> packageBuilderSupplier;

    public NewWingPackageDecoder(final String packagePrefix, final Supplier<BUILDER> packageBuilderSupplier) {
        super(packagePrefix);
        this.packageBuilderSupplier = packageBuilderSupplier;
    }

    @Override
    public final PACKAGE decode(final ByteBuf buffer) {
        final BUILDER packageBuilder = this.packageBuilderSupplier.get();
        this.decodeUntilChecksum(buffer, packageBuilder);
        this.decodeChecksum(buffer, packageBuilder);
        return packageBuilder.build();
    }

    protected abstract void decodeUntilChecksum(final ByteBuf buffer, final BUILDER packageBuilder);

    private void decodeChecksum(final ByteBuf buffer, final BUILDER packageBuilder) {
        final int checksum = buffer.readIntLE();
        packageBuilder.setChecksum(checksum);
    }
}
