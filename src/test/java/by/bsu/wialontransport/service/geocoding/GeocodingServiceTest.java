//package by.bsu.wialontransport.service.geocoding;
//
//import by.bsu.wialontransport.crud.dto.Address;
//import by.bsu.wialontransport.crud.dto.Data.Latitude;
//import by.bsu.wialontransport.crud.dto.Data.Longitude;
//import lombok.Getter;
//import org.junit.Test;
//
//import java.util.Optional;
//
//import static by.bsu.wialontransport.crud.entity.DataEntity.Latitude.Type.NORTH;
//import static by.bsu.wialontransport.crud.entity.DataEntity.Longitude.Type.EAST;
//import static java.util.Optional.empty;
//import static org.junit.Assert.assertEquals;
//
//public final class GeocodingServiceTest {
//    private final TestGeocodingService geocodingService = new TestGeocodingService();
//
//    @Test
//    public void addressShouldBeReceivedByLatitudeAndLongitude() {
//        final Latitude givenLatitude = Latitude.builder()
//                .degrees(50)
//                .minutes(30)
//                .minuteShare(20)
//                .type(NORTH)
//                .build();
//        final Longitude givenLongitude = Longitude.builder()
//                .degrees(60)
//                .minutes(40)
//                .minuteShare(30)
//                .type(EAST)
//                .build();
//
//        this.geocodingService.receive(givenLatitude, givenLongitude);
//
//        final double actualCapturedLatitude = this.geocodingService.getCapturedLatitude();
//        final double actualCapturedLongitude = this.geocodingService.getCapturedLongitude();
//        final double expectedCapturedLatitude = 50.50555555555555;
//        final double expectedCapturedLongitude = 60.675;
//        assertEquals(expectedCapturedLatitude, actualCapturedLatitude, 0.);
//        assertEquals(expectedCapturedLongitude, actualCapturedLongitude, 0.);
//    }
//
//    @Test
//    public void nameOfServiceShouldBeFound() {
//        final String actual = this.geocodingService.findName();
//        final String expected = "TestGeocodingService";
//        assertEquals(expected, actual);
//    }
//
//    @Getter
//    private static final class TestGeocodingService implements GeocodingService {
//        private double capturedLatitude;
//        private double capturedLongitude;
//
//        @Override
//        public Optional<Address> receive(final double latitude, final double longitude) {
//            this.capturedLatitude = latitude;
//            this.capturedLongitude = longitude;
//            return empty();
//        }
//    }
//}
