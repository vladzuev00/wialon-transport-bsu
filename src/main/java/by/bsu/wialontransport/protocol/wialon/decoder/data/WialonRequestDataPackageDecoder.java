//package by.bsu.wialontransport.protocol.wialon.decoder.packages.data;
//
//
//import by.bsu.wialontransport.protocol.wialon.decoder.packages.data.parser.WialonMessageParser;
//import by.bsu.wialontransport.protocol.wialon.model.WialonData;
//import by.bsu.wialontransport.protocol.wialon.model.packages.data.request.WialonRequestDataPackage;
//import by.bsu.wialontransport.protocol.wialon.model.packages.data.response.WialonResponseDataPackage;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//import static by.bsu.wialontransport.protocol.wialon.model.packages.data.request.WialonRequestDataPackage.PREFIX;
//import static by.bsu.wialontransport.protocol.wialon.model.packages.data.response.WialonResponseDataPackage.Status.ERROR_PACKAGE_STRUCTURE;
//
//@Component
//public final class WialonRequestDataPackageDecoder extends AbstractWialonRequestDataPackageDecoder {
//
//    public WialonRequestDataPackageDecoder(final WialonMessageParser messageParser) {
//        super(PREFIX, messageParser);
//    }
//
//    @Override
//    protected WialonRequestDataPackage createPackage(final List<WialonData> data) {
//        checkContainingOneData(data);
//        final WialonData firstData = data.get(0);
//        return new WialonRequestDataPackage(firstData);
//    }
//
//    @Override
//    protected WialonResponseDataPackage createNotValidSubMessageResponse() {
//        return new WialonResponseDataPackage(ERROR_PACKAGE_STRUCTURE);
//    }
//
//    private static void checkContainingOneData(final List<WialonData> data) {
//        if (data.size() != 1) {
//            throw new IllegalArgumentException("Data package should contain only one message");
//        }
//    }
//}
