//package by.bsu.wialontransport.protocol.wialon.decoder.packages.data;
//
//import by.bsu.wialontransport.protocol.wialon.model.WialonData;
//import by.bsu.wialontransport.protocol.wialon.model.packages.data.request.WialonRequestDataPackage;
//import by.bsu.wialontransport.protocol.wialon.model.packages.data.response.WialonResponseDataPackage;
//import org.junit.Test;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import static by.bsu.wialontransport.protocol.wialon.model.packages.data.response.WialonResponseDataPackage.Status.ERROR_PACKAGE_STRUCTURE;
//import static java.util.Collections.emptyList;
//import static org.junit.Assert.assertEquals;
//
//public final class WialonRequestDataPackageDecoderTest {
//    private final WialonRequestDataPackageDecoder decoder = new WialonRequestDataPackageDecoder(null);
//
//    @Test
//    public void packageShouldBeCreated() {
//        final WialonData givenData = createData(LocalDate.of(2024, 1, 16));
//        final List<WialonData> givenDataAsList = List.of(givenData);
//
//        final WialonRequestDataPackage actual = decoder.createPackage(givenDataAsList);
//        final WialonRequestDataPackage expected = new WialonRequestDataPackage(givenData);
//        assertEquals(expected, actual);
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void packageShouldNotBeCreatedBecauseOfThereIsNoData() {
//        final List<WialonData> givenData = emptyList();
//
//        decoder.createPackage(givenData);
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void packageShouldNotBeCreatedBecauseOfThereAreSeveralData() {
//        final List<WialonData> givenData = List.of(
//                createData(LocalDate.of(2024, 1, 16)),
//                createData(LocalDate.of(2024, 1, 17))
//        );
//
//        decoder.createPackage(givenData);
//    }
//
//    @Test
//    public void notValidSubMessageResponseShouldBeCreated() {
//        final WialonResponseDataPackage actual = decoder.createNotValidSubMessageResponse();
//        final WialonResponseDataPackage expected = new WialonResponseDataPackage(ERROR_PACKAGE_STRUCTURE);
//        assertEquals(expected, actual);
//    }
//
//    private static WialonData createData(final LocalDate date) {
//        return WialonData.builder()
//                .date(date)
//                .build();
//    }
//}
