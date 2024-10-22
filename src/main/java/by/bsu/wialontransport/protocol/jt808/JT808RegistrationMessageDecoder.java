//package by.bsu.wialontransport.protocol.jt808;
//
//import by.zuevvlad.jt808.model.JT808RegistrationMessage;
//import io.netty.buffer.ByteBuf;
//import org.springframework.stereotype.Component;
//
//import static by.zuevvlad.jt808.util.JT808Util.decodeString;
//
//@Component
//public final class JT808RegistrationMessageDecoder extends JT808MessageDecoder<JT808RegistrationMessage> {
//    private static final short MESSAGE_ID = 256;
//    private static final int MANUFACTURER_ID_BYTE_COUNT = 5;
//
//    public JT808RegistrationMessageDecoder() {
//        super(MESSAGE_ID);
//    }
//
//    @Override
//    protected JT808RegistrationMessage decodeInternal(ByteBuf buffer, String phoneNumber) {
//        skipProvinceId(buffer);
//        skipCityId(buffer);
//        String manufacturerId = decodeManufacturerId(buffer);
//        return new JT808RegistrationMessage(phoneNumber, manufacturerId);
//    }
//
//    private void skipProvinceId(ByteBuf buffer) {
//        buffer.skipBytes(Short.BYTES);
//    }
//
//    private void skipCityId(ByteBuf buffer) {
//        buffer.skipBytes(Short.BYTES);
//    }
//
//    private String decodeManufacturerId(ByteBuf buffer) {
//        return decodeString(buffer, MANUFACTURER_ID_BYTE_COUNT);
//    }
//}
