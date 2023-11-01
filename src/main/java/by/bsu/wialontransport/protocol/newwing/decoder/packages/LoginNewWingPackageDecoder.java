package by.bsu.wialontransport.protocol.newwing.decoder.packages;

import by.bsu.wialontransport.protocol.newwing.model.packages.LoginNewWingPackage;
import by.bsu.wialontransport.protocol.newwing.model.packages.builder.LoginNewWingPackageBuilder;
import io.netty.buffer.ByteBuf;

public final class LoginNewWingPackageDecoder
        extends NewWingPackageDecoder<LoginNewWingPackage, LoginNewWingPackageBuilder> {
    private static final String PACKAGE_PREFIX = "GPRSGC";

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
        return Short.toString(imei);
    }
}
