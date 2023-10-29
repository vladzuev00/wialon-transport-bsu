package by.bsu.wialontransport.protocol.wialon.encoder;

import by.bsu.wialontransport.protocol.wialon.encoder.chain.StarterPackageEncoder;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@RequiredArgsConstructor
public final class WialonEncoder extends MessageToByteEncoder<WialonPackage> {
    private static final String TEMPLATE_MESSAGE_START_ENCODING = "Start encoding outbound package: '%s'.";

    private final StarterPackageEncoder starterPackageEncoder;

    @Override
    protected void encode(final ChannelHandlerContext channelHandlerContext, final WialonPackage responsePackage,
                          final ByteBuf byteBuf) {
        log.info(format(TEMPLATE_MESSAGE_START_ENCODING, responsePackage));
        final String encodedResponsePackage = this.starterPackageEncoder.encode(responsePackage);
        byteBuf.writeCharSequence(encodedResponsePackage, UTF_8);
    }
}
