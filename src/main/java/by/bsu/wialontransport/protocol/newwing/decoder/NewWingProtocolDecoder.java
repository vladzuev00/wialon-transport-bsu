package by.bsu.wialontransport.protocol.newwing.decoder;

import by.bsu.wialontransport.protocol.core.decoder.ProtocolBufferDecoder;
import by.bsu.wialontransport.protocol.newwing.decoder.packages.NewWingPackageDecoder;
import by.bsu.wialontransport.protocol.newwing.model.packages.NewWingPackage;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class NewWingProtocolDecoder extends ProtocolBufferDecoder<String, NewWingPackage, NewWingPackageDecoder<?, ?>> {
    private static final int PACKAGE_PREFIX_LENGTH = 6;
    private static final Charset PACKAGE_PREFIX_CHARSET = UTF_8;

    public NewWingProtocolDecoder(final List<NewWingPackageDecoder<?, ?>> packageDecoders) {
        super(packageDecoders);
    }

    @Override
    protected String extractPackagePrefix(final ByteBuf buffer) {
        final CharSequence packagePrefix = buffer.readCharSequence(PACKAGE_PREFIX_LENGTH, PACKAGE_PREFIX_CHARSET);
        return packagePrefix.toString();
    }
}