//package by.bsu.wialontransport.protocol.jt808;
//
//import io.netty.buffer.ByteBuf;
//import org.springframework.stereotype.Component;
//
//@Component
//public final class JT808BulkLocationMessageDecoder extends JT808LocationMessageDecoder {
//    private static final short MESSAGE_ID = 1796;
//
//    public JT808BulkLocationMessageDecoder() {
//        super(MESSAGE_ID);
//    }
//
//    @Override
//    protected int decodeLocationCount(ByteBuf buffer) {
//        return buffer.readShort();
//    }
//
//    @Override
//    protected void skipUntilFirstLocation(ByteBuf buffer) {
//        buffer.skipBytes(Byte.BYTES);
//    }
//
//    @Override
//    protected void skipLocationPrefix(ByteBuf buffer) {
//        buffer.skipBytes(Short.BYTES);
//    }
//}
