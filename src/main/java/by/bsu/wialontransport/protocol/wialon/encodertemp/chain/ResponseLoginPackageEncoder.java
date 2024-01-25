package by.bsu.wialontransport.protocol.wialon.encodertemp.chain;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.login.WialonResponseLoginPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.login.WialonResponseLoginPackage.Status;
import org.springframework.stereotype.Component;

import static by.bsu.wialontransport.protocol.wialon.wialonpackage.login.WialonResponseLoginPackage.PREFIX;
import static java.lang.String.format;

@Component
public final class ResponseLoginPackageEncoder extends PackageEncoder {
    private static final String TEMPLATE_ENCODED_PACKAGE_WITHOUT_POSTFIX = PREFIX + "%s";

    public ResponseLoginPackageEncoder(final ResponsePingPackageEncoder nextEncoder) {
        super(WialonResponseLoginPackage.class, nextEncoder);
    }

    @Override
    protected String encodeIndependentlyWithoutPostfix(final WialonPackage encodedPackage) {
        final WialonResponseLoginPackage responseLoginPackage = (WialonResponseLoginPackage) encodedPackage;
        final Status status = responseLoginPackage.getStatus();
        return format(TEMPLATE_ENCODED_PACKAGE_WITHOUT_POSTFIX, status.getValue());
    }
}
