package by.bsu.wialontransport.protocol.wialon.encoder.chain;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.Package;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.ResponseBlackBoxPackage;
import org.springframework.stereotype.Component;

import static by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.ResponseBlackBoxPackage.PREFIX;
import static java.lang.String.format;

@Component
public final class ResponseBlackBoxPackageEncoder extends PackageEncoder {

    private static final String TEMPLATE_ENCODED_PACKAGE_WITHOUT_POSTFIX = PREFIX + "%d";

    public ResponseBlackBoxPackageEncoder(final FinisherPackageEncoder nextEncoder) {
        super(ResponseBlackBoxPackage.class, nextEncoder);
    }

    @Override
    protected String encodeIndependentlyWithoutPostfix(final Package encodedPackage) {
        final ResponseBlackBoxPackage responseBlackBoxPackage = (ResponseBlackBoxPackage) encodedPackage;
        return format(TEMPLATE_ENCODED_PACKAGE_WITHOUT_POSTFIX, responseBlackBoxPackage.getAmountFixedMessages());
    }
}
