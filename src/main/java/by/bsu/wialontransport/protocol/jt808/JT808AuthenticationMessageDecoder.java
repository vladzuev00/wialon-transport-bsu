//package by.bsu.wialontransport.protocol.jt808;
//
//import by.zuevvlad.jt808.model.JT808AuthenticationMessage;
//import io.netty.buffer.ByteBuf;
//import org.springframework.stereotype.Component;
//
//@Component
//public final class JT808AuthenticationMessageDecoder extends JT808MessageDecoder<JT808AuthenticationMessage> {
//    private static final short MESSAGE_ID = 258;
//
//    public JT808AuthenticationMessageDecoder() {
//        super(MESSAGE_ID);
//    }
//
//    @Override
//    protected JT808AuthenticationMessage decodeInternal(ByteBuf buffer, String phoneNumber) {
//        return new JT808AuthenticationMessage();
//    }
//}
