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
    static final Charset SOURCE_CHARSET = UTF_8;

    private final Pattern prefixPattern;

    public ProtocolStringDecoder(final List<? extends PackageStringDecoder<?>> packageDecoders, final String prefixRegex) {
        super(packageDecoders);
        prefixPattern = compile(prefixRegex);
    }

    @Override
    protected final String createSource(final ByteBuf buffer) {
        return buffer.readCharSequence(buffer.readableBytes(), SOURCE_CHARSET).toString();
    }

    @Override
    protected final String getPrefix(final String source) {
        return prefixPattern.matcher(source)
                .results()
                .map(MatchResult::group)
                .findFirst()
                .orElseThrow(() -> createPrefixExtractingException(source));
    }

    private PrefixExtractingException createPrefixExtractingException(final String source) {
        return new PrefixExtractingException("Impossible to extract prefix from '%s'".formatted(source));
    }

    static final class PrefixExtractingException extends RuntimeException {

        @SuppressWarnings("unused")
        public PrefixExtractingException() {

        }

        public PrefixExtractingException(final String description) {
            super(description);
        }

        @SuppressWarnings("unused")
        public PrefixExtractingException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public PrefixExtractingException(final String description, final Exception cause) {
            super(description, cause);
        }
    }
}
