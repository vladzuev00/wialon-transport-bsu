package by.bsu.wialontransport.protocol.wialon.encoder.chain;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.login.ResponseLoginPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.login.ResponseLoginPackage.Status;
import org.springframework.stereotype.Component;

import static by.bsu.wialontransport.protocol.wialon.wialonpackage.login.ResponseLoginPackage.PREFIX;
import static java.lang.String.format;

@Component
public final class ResponseLoginPackageEncoder extends PackageEncoder {
    private static final String TEMPLATE_ENCODED_PACKAGE_WITHOUT_POSTFIX = PREFIX + "%s";

    public ResponseLoginPackageEncoder(final ResponsePingPackageEncoder nextEncoder) {
        super(ResponseLoginPackage.class, nextEncoder);
    }

    @Override
    protected String encodeIndependentlyWithoutPostfix(final WialonPackage encodedPackage) {
        final ResponseLoginPackage responseLoginPackage = (ResponseLoginPackage) encodedPackage;
        final Status status = responseLoginPackage.getStatus();
        return format(TEMPLATE_ENCODED_PACKAGE_WITHOUT_POSTFIX, status.getValue());
    }
}
