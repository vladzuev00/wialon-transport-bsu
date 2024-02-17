package by.bsu.wialontransport.protocol.core.decoder;

import by.bsu.wialontransport.protocol.core.decoder.packages.PackageStringDecoder;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.regex.Pattern.compile;

public abstract class ProtocolStringDecoder extends ProtocolDecoder<String, String> {
    private static final Charset SOURCE_CHARSET = UTF_8;

    private final Pattern packagePrefixPattern;

    public ProtocolStringDecoder(final List<? extends PackageStringDecoder<?>> packageDecoders,
                                 final String packagePrefixRegex) {
        super(packageDecoders);
        packagePrefixPattern = compile(packagePrefixRegex);
    }

    @Override
    protected final String createSource(final ByteBuf buffer) {
        return buffer.readCharSequence(buffer.readableBytes(), SOURCE_CHARSET).toString();
    }

    @Override
    protected final String extractPackagePrefix(final String source) {
        return packagePrefixPattern.matcher(source)
                .results()
                .map(MatchResult::group)
                .findFirst()
                .orElseThrow(
                        () -> new PackagePrefixExtractingException(
                                "Impossible to extract package's prefix from '%s'".formatted(source)
                        )
                );
    }

    static final class PackagePrefixExtractingException extends RuntimeException {

        @SuppressWarnings("unused")
        public PackagePrefixExtractingException() {

        }

        public PackagePrefixExtractingException(final String description) {
            super(description);
        }

        @SuppressWarnings("unused")
        public PackagePrefixExtractingException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public PackagePrefixExtractingException(final String description, final Exception cause) {
            super(description, cause);
        }
    }
}
