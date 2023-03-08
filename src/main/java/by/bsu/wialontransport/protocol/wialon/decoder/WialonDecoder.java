package by.bsu.wialontransport.protocol.wialon.decoder;

import by.bsu.wialontransport.protocol.wialon.decoder.chain.StarterPackageDecoder;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.Package;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static by.bsu.wialontransport.protocol.wialon.wialonpackage.Package.POSTFIX;
import static java.util.stream.IntStream.range;

@Slf4j
public final class WialonDecoder extends ReplayingDecoder<Package> {
    private static final String TEMPLATE_MESSAGE_START_DECODING_INBOUND_PACKAGE
            = "Start decoding inbound package: '{}'.";
    private static final char CHARACTER_OF_END_REQUEST_PACKAGE = '\n';

    private final StarterPackageDecoder starterPackageDecoder;

    public WialonDecoder(final StarterPackageDecoder starterPackageDecoder) {
        this.starterPackageDecoder = starterPackageDecoder;
    }

    @Override
    protected void decode(final ChannelHandlerContext context, final ByteBuf byteBuf, final List<Object> outObjects) {
        final String serializedPackage = findSerializedPackage(byteBuf);
        log.info(TEMPLATE_MESSAGE_START_DECODING_INBOUND_PACKAGE, serializedPackage);
        final Package requestPackage = this.starterPackageDecoder.decode(serializedPackage);
        outObjects.add(requestPackage);
    }

    private static String findSerializedPackage(final ByteBuf byteBuf) {
        final StringBuilder requestPackageBuilder = new StringBuilder();
        char currentAppendedCharacter;
        do {
            currentAppendedCharacter = (char) byteBuf.readByte();
            requestPackageBuilder.append(currentAppendedCharacter);
        } while (byteBuf.isReadable() && currentAppendedCharacter != CHARACTER_OF_END_REQUEST_PACKAGE);
        removePostfix(requestPackageBuilder);
        return requestPackageBuilder.toString();
    }

    private static void removePostfix(final StringBuilder requestPackageBuilder) {
        range(0, POSTFIX.length())
                .forEach(i -> requestPackageBuilder.deleteCharAt(requestPackageBuilder.length() - 1));
    }
}