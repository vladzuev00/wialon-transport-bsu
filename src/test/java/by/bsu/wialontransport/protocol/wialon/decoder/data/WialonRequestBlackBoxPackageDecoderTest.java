//package by.bsu.wialontransport.protocol.wialon.decoder.packages.data;
//
//import by.bsu.wialontransport.protocol.wialon.model.WialonData;
//import by.bsu.wialontransport.protocol.wialon.model.packages.data.request.WialonRequestBlackBoxPackage;
//import by.bsu.wialontransport.protocol.wialon.model.packages.data.response.WialonResponseBlackBoxPackage;
//import org.junit.Test;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import static org.junit.Assert.assertEquals;
//
//public final class WialonRequestBlackBoxPackageDecoderTest {
//    private final WialonRequestBlackBoxPackageDecoder decoder = new WialonRequestBlackBoxPackageDecoder(null);
//
//    @Test
//    public void packageShouldBeCreated() {
//        final List<WialonData> givenData = List.of(
//                createData(LocalDate.of(2024, 1, 16)),
//                createData(LocalDate.of(2024, 1, 17)),
//                createData(LocalDate.of(2024, 1, 18))
//        );
//
//        final WialonRequestBlackBoxPackage actual = decoder.createPackage(givenData);
//        final WialonRequestBlackBoxPackage expected = new WialonRequestBlackBoxPackage(givenData);
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void responseNotValidDataPackageShouldBeCreated() {
//        final WialonResponseBlackBoxPackage actual = decoder.createNotValidSubMessageResponse();
//        final WialonResponseBlackBoxPackage expected = new WialonResponseBlackBoxPackage(0);
//        assertEquals(expected, actual);
//    }
//
//    private static WialonData createData(final LocalDate date) {
//        return WialonData.builder()
//                .date(date)
//                .build();
//    }
//}
