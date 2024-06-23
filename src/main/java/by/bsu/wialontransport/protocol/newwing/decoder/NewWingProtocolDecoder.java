//package by.bsu.wialontransport.protocol.newwing.decoder;
//
//import by.bsu.wialontransport.protocol.newwing.decoder.packages.NewWingPackageDecoder;
//import io.netty.buffer.ByteBuf;
//
//import java.nio.charset.Charset;
//import java.util.List;
//
//import static java.nio.charset.StandardCharsets.UTF_8;
//
//public final class NewWingProtocolDecoder extends ProtocolBufferDecoder<String> {
//    static final int PREFIX_LENGTH = 6;
//    static final Charset PREFIX_CHARSET = UTF_8;
//
//    public NewWingProtocolDecoder(final List<NewWingPackageDecoder> packageDecoders) {
//        super(packageDecoders);
//    }
//
//    @Override
//    protected String getPrefix(final ByteBuf buffer) {
//        return buffer.readCharSequence(PREFIX_LENGTH, PREFIX_CHARSET).toString();
//    }
//}
