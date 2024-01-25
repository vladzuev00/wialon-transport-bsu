package by.bsu.wialontransport.protocol.wialon.encodertemp.chain;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.WialonResponseDataPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.WialonResponseDataPackage.Status;
import org.springframework.stereotype.Component;

import static by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.WialonResponseDataPackage.PREFIX;
import static java.lang.String.format;

@Component
public final class ResponseDataPackageEncoder extends PackageEncoder {
    private static final String TEMPLATE_ENCODED_PACKAGE_WITHOUT_POSTFIX = PREFIX + "%d";

    public ResponseDataPackageEncoder(final ResponseBlackBoxPackageEncoder nextEncoder) {
        super(WialonResponseDataPackage.class, nextEncoder);
    }

    @Override
    protected String encodeIndependentlyWithoutPostfix(final WialonPackage encodedPackage) {
        final WialonResponseDataPackage responseDataPackage = (WialonResponseDataPackage) encodedPackage;
        final Status status = responseDataPackage.getStatus();
        return format(TEMPLATE_ENCODED_PACKAGE_WITHOUT_POSTFIX, status.getValue());
    }
}
