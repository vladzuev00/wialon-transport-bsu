//package by.vladzuev.locationreceiver.crud.dto;
//
//import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
//import by.vladzuev.locationreceiver.util.GeometryTestUtil;
//import org.junit.Test;
//import org.locationtech.jts.geom.Geometry;
//import org.locationtech.jts.geom.GeometryFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import static by.vladzuev.locationreceiver.util.GeometryTestUtil.createPolygon;
//import static org.junit.Assert.assertEquals;
//
//public final class CityTest extends AbstractSpringBootTest {
//
//    @Autowired
//    private GeometryFactory geometryFactory;
//
//    @Test
//    public void cityShouldBeCopiedWithGivenAddressAndProcess() {
//        final City givenSource = createCity(255L);
//        final Address givenAddress = createAddress(256L);
//        final SearchingCitiesProcess givenProcess = createProcess(257L);
//
//        final City actual = City.copyWithAddressAndProcess(givenSource, givenAddress, givenProcess);
//        final City expected = new City(255L, givenAddress, givenProcess);
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void cityShouldBeCreatedByAddress() {
//        final Address givenAddress = createAddress(256L);
//
//        final City actual = City.createWithAddress(givenAddress);
//        final City expected = createCity(givenAddress);
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void geometryOfCityShouldBeFound() {
//        final Geometry givenGeometry = GeometryTestUtil.createPolygon(
//                geometryFactory,
//                1.1, 2.2, 3.3, 4.4, 5.5, 6.6
//        );
//        final Address givenAddress = createAddress(givenGeometry);
//        final City givenCity = createCity(givenAddress);
//
//        final Geometry actual = givenCity.findGeometry();
//        assertEquals(givenGeometry, actual);
//    }
//
//    @SuppressWarnings("SameParameterValue")
//    private static City createCity(final Long id) {
//        return City.builder()
//                .id(id)
//                .build();
//    }
//
//    private static City createCity(final Address address) {
//        return City.builder()
//                .address(address)
//                .build();
//    }
//
//    @SuppressWarnings("SameParameterValue")
//    private static Address createAddress(final Long id) {
//        return Address.builder()
//                .id(id)
//                .build();
//    }
//
//    private static Address createAddress(final Geometry geometry) {
//        return Address.builder()
//                .geometry(geometry)
//                .build();
//    }
//
//    @SuppressWarnings("SameParameterValue")
//    private static SearchingCitiesProcess createProcess(final Long id) {
//        return SearchingCitiesProcess.builder()
//                .id(id)
//                .build();
//    }
//}
