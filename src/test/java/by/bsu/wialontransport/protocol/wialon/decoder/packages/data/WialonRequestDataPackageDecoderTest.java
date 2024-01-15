//package by.bsu.wialontransport.protocol.wialon.decoder.packages.data;
//
//import by.bsu.wialontransport.crud.dto.Data;
//import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request.WialonRequestDataPackage;
//import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.WialonResponseDataPackage;
//import org.junit.Test;
//
//import java.util.List;
//import java.util.stream.Stream;
//
//import static by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.WialonResponseDataPackage.Status.ERROR_PACKAGE_STRUCTURE;
//import static java.util.Collections.emptyList;
//import static org.junit.Assert.assertEquals;
//
//public final class WialonRequestDataPackageDecoderTest {
//    private final WialonRequestDataPackageDecoder decoder = new WialonRequestDataPackageDecoder(null);
//
//    @Test
//    public void messageShouldBeSplittedIntoSubMessages() {
//        final String givenMessage = "message";
//
//        final Stream<String> actual = this.decoder.splitIntoSubMessages(givenMessage);
//        final List<String> actualAsList = actual.toList();
//        final List<String> expectedAsList = List.of(givenMessage);
//        assertEquals(expectedAsList, actualAsList);
//    }
//
//    @Test
//    public void packageShouldBeCreated() {
//        final Data givenData = createData(255L);
//        final List<Data> givenDataAsList = List.of(givenData);
//
//        final WialonRequestDataPackage actual = this.decoder.createPackage(givenDataAsList);
//        final WialonRequestDataPackage expected = new WialonRequestDataPackage(givenData);
//        assertEquals(expected, actual);
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void packageShouldNotBeCreatedBecauseOfThereIsNoData() {
//        final List<Data> givenData = emptyList();
//
//        this.decoder.createPackage(givenData);
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void packageShouldNotBeCreatedBecauseOfThereAreSeveralData() {
//        final List<Data> givenData = List.of(
//                createData(1L),
//                createData(2L)
//        );
//
//        this.decoder.createPackage(givenData);
//    }
//
//    @Test
//    public void responseNotValidDataPackageShouldBeCreated() {
//        final WialonResponseDataPackage actual = this.decoder.createResponseNotValidDataPackage();
//        final WialonResponseDataPackage expected = new WialonResponseDataPackage(ERROR_PACKAGE_STRUCTURE);
//        assertEquals(expected, actual);
//    }
//
//    private static Data createData(final Long id) {
//        return Data.builder()
//                .id(id)
//                .build();
//    }
//}
