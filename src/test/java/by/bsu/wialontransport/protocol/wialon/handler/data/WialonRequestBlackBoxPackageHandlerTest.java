//package by.bsu.wialontransport.protocol.wialon.handler.packages.data;
//
//import by.bsu.wialontransport.protocol.wialon.model.packages.data.response.WialonResponseBlackBoxPackage;
//import org.junit.Test;
//
//import static org.junit.Assert.assertEquals;
//
//public final class WialonRequestBlackBoxPackageHandlerTest {
//    private final WialonRequestBlackBoxPackageHandler handler = new WialonRequestBlackBoxPackageHandler(
//            null,
//            null,
//            null,
//            null
//    );
//
//    @Test
//    public void responseShouldBeCreated() {
//        final int givenReceivedDataCount = 5;
//
//        final WialonResponseBlackBoxPackage actual = handler.createResponse(givenReceivedDataCount);
//        final WialonResponseBlackBoxPackage expected = new WialonResponseBlackBoxPackage(givenReceivedDataCount);
//        assertEquals(expected, actual);
//    }
//}
