package by.bsu.wialontransport.protocol.newwing.decoder.packages;

import by.bsu.wialontransport.protocol.newwing.model.packages.request.NewWingLoginPackage;
import by.bsu.wialontransport.protocol.newwing.model.packages.request.builder.LoginNewWingPackageBuilder;
import io.netty.buffer.ByteBuf;

import static java.lang.String.format;

public final class LoginNewWingPackageDecoder
        extends NewWingPackageDecoder<NewWingLoginPackage, LoginNewWingPackageBuilder> {
    private static final String PACKAGE_PREFIX = "GPRSGC";
    private static final String TEMPLATE_IMEI = "%020d";

    public LoginNewWingPackageDecoder() {
        super(PACKAGE_PREFIX, LoginNewWingPackageBuilder::new);
    }

    @Override
    protected void decodeUntilChecksum(final ByteBuf buffer, final LoginNewWingPackageBuilder packageBuilder) {
        decodeImei(buffer, packageBuilder);
    }

    private static void decodeImei(final ByteBuf buffer, final LoginNewWingPackageBuilder packageBuilder) {
        final String imei = readImei(buffer);
        packageBuilder.setImei(imei);
    }

    private static String readImei(final ByteBuf buffer) {
        final short imei = buffer.readShortLE();
        return format(TEMPLATE_IMEI, imei);
    }
}
