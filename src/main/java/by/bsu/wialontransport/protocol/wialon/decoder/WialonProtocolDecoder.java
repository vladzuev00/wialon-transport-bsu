package by.bsu.wialontransport.protocol.wialon.decoder;

import by.bsu.wialontransport.protocol.core.decoder.ProtocolStringDecoder;
import by.bsu.wialontransport.protocol.wialon.decoder.packages.WialonPackageDecoder;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

public final class WialonProtocolDecoder extends ProtocolStringDecoder<WialonPackageDecoder<?>> {
    private static final String PACKAGE_PREFIX_REGEX = "^#.+#";
    private static final Pattern PACKAGE_PREFIX_PATTERN = compile(PACKAGE_PREFIX_REGEX);

    public WialonProtocolDecoder(final List<WialonPackageDecoder<?>> packageDecoders) {
        super(packageDecoders);
    }

    //TODO: refactor
    @Override
    protected String extractPackagePrefix(final String source) {
        final Matcher matcher = PACKAGE_PREFIX_PATTERN.matcher(source);
        if (matcher.find()) {
            return matcher.group();
        } else {
            throw new PackagePrefixExtractingException(
                    "Impossible to extract package's prefix from '%s'".formatted(source)
            );
        }
    }

    private static final class PackagePrefixExtractingException extends RuntimeException {

        public PackagePrefixExtractingException() {

        }

        public PackagePrefixExtractingException(final String description) {
            super(description);
        }

        public PackagePrefixExtractingException(final Exception cause) {
            super(cause);
        }

        public PackagePrefixExtractingException(final String description, final Exception cause) {
            super(description, cause);
        }
    }
}
