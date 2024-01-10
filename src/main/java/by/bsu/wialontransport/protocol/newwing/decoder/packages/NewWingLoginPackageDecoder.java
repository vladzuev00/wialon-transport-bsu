package by.bsu.wialontransport.protocol.newwing.decoder.packages;

import by.bsu.wialontransport.protocol.newwing.model.packages.request.NewWingLoginPackage;
import by.bsu.wialontransport.protocol.newwing.model.packages.request.builder.NewWingLoginPackageBuilder;
import io.netty.buffer.ByteBuf;

import static java.lang.String.format;

public final class NewWingLoginPackageDecoder
        extends NewWingPackageDecoder<NewWingLoginPackage, NewWingLoginPackageBuilder> {
    private static final String PACKAGE_PREFIX = "GPRSGC";
    private static final String TEMPLATE_IMEI = "%020d";

    public NewWingLoginPackageDecoder() {
        super(PACKAGE_PREFIX);
    }

    @Override
    protected NewWingLoginPackageBuilder createPackageBuilder() {
        return new NewWingLoginPackageBuilder();
    }

    @Override
    protected void decodeUntilChecksum(final ByteBuf buffer, final NewWingLoginPackageBuilder packageBuilder) {
        decodeImei(buffer, packageBuilder);
    }

    private static void decodeImei(final ByteBuf buffer, final NewWingLoginPackageBuilder packageBuilder) {
        final short value = buffer.readShortLE();
        final String imei = format(TEMPLATE_IMEI, value);
        packageBuilder.setImei(imei);
    }
}
