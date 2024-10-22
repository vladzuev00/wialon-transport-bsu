//package by.bsu.wialontransport.protocol.jt808;
//
//import io.netty.buffer.ByteBuf;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//public final class JT808MessagesDecoder {
//    static final int IDENTIFICATION_BYTE_COUNT = 1;
//
//    private final List<JT808MessageDecoder<?>> decoders;
//
//    public Object decode(ByteBuf buffer) {
//        buffer.retain();
//        try {
//            ByteBuf trimmedBuffer = trimIdentification(buffer);
//            short messageId = trimmedBuffer.readShort();
//            return findDecoder(messageId).decode(trimmedBuffer);
//        } finally {
//            buffer.release();
//        }
//    }
//
//    private ByteBuf trimIdentification(ByteBuf buffer) {
//        return buffer.slice(IDENTIFICATION_BYTE_COUNT, buffer.writerIndex() - 1 - IDENTIFICATION_BYTE_COUNT);
//    }
//
//    private JT808MessageDecoder<?> findDecoder(short messageId) {
//        return decoders.stream()
//                .filter(decoder -> decoder.isAbleDecode(messageId))
//                .findFirst()
//                .orElseThrow(() -> new IllegalArgumentException("No decoder for message's id '%d'".formatted(messageId)));
//    }
//}
