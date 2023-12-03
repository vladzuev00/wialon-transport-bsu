//package by.bsu.wialontransport.service.geocoding.aspect;
//
//import by.bsu.wialontransport.base.AbstractContextTest;
//import by.bsu.wialontransport.crud.dto.Address;
//import by.bsu.wialontransport.crud.dto.Data.Latitude;
//import by.bsu.wialontransport.crud.dto.Data.Longitude;
//import by.bsu.wialontransport.service.geocoding.GeocodingService;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Captor;
//import org.mockito.Mock;
//import org.slf4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Import;
//import org.springframework.stereotype.Service;
//import sun.misc.Unsafe;
//
//import java.lang.reflect.Field;
//import java.util.Optional;
//
//import static java.util.Optional.empty;
//import static org.junit.Assert.assertEquals;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//
//@Import({
//        GeocodingServiceAspectTest.SuccessfullyReceivingService.class,
//        GeocodingServiceAspectTest.FailureReceivingService.class
//})
//public final class GeocodingServiceAspectTest extends AbstractContextTest {
//    private static final String FIELD_NAME_LOGGER = "log";
//    private static final String FIELD_NAME_THE_UNSAFE = "theUnsafe";
//
//    @Mock
//    private Logger mockedLogger;
//
//    @Captor
//    private ArgumentCaptor<String> stringArgumentCaptor;
//
//    @Autowired
//    private SuccessfullyReceivingService successfullyReceivingService;
//
//    @Autowired
//    private FailureReceivingService failureReceivingService;
//
//    @Before
//    public void injectMockedLogger()
//            throws Exception {
//        final Field unsafeField = Unsafe.class.getDeclaredField(FIELD_NAME_THE_UNSAFE);
//        unsafeField.setAccessible(true);
//        try {
//            final Unsafe unsafe = (Unsafe) unsafeField.get(null);
//            final Field ourField = GeocodingServiceAspect.class.getDeclaredField(FIELD_NAME_LOGGER);
//            final Object staticFieldBase = unsafe.staticFieldBase(ourField);
//            final long staticFieldOffset = unsafe.staticFieldOffset(ourField);
//            unsafe.putObject(staticFieldBase, staticFieldOffset, this.mockedLogger);
//        } finally {
//            unsafeField.setAccessible(false);
//        }
//    }
//
//    @Test
//    public void successReceivingByDoubleLatitudeAndDoubleLongitudeShouldBeLogged() {
//        this.successfullyReceivingService.receive(4.5, 5.5);
//
//        verify(this.mockedLogger, times(1)).info(this.stringArgumentCaptor.capture());
//        assertEquals(
//                "Address "
//                        + "'Address(id=null, boundingBox=null, center=null, cityName=null, countryName=null, geometry=null)' "
//                        + "was successfully received by 'SuccessfullyReceivingService'",
//                this.stringArgumentCaptor.getValue()
//        );
//    }
//
//    @Test
//    public void failureReceivingByDoubleLatitudeAndDoubleLongitudeShouldBeLogged() {
//        this.failureReceivingService.receive(4.5, 5.5);
//
//        verify(this.mockedLogger, times(1)).info(this.stringArgumentCaptor.capture());
//        assertEquals(
//                "Address wasn't received by 'FailureReceivingService'",
//                this.stringArgumentCaptor.getValue()
//        );
//    }
//
//    @Test
//    public void successReceivingByLatitudeAndLongitudeShouldBeLogged() {
//        final Latitude givenLatitude = createLatitude();
//        final Longitude givenLongitude = createLongitude();
//
//        this.successfullyReceivingService.receive(givenLatitude, givenLongitude);
//
//        verify(this.mockedLogger, times(1)).info(this.stringArgumentCaptor.capture());
//        assertEquals(
//                "Address "
//                        + "'Address(id=null, boundingBox=null, center=null, cityName=null, countryName=null, geometry=null)' "
//                        + "was successfully received by 'SuccessfullyReceivingService'",
//                this.stringArgumentCaptor.getValue()
//        );
//    }
//
//    @Test
//    public void failureReceivingByLatitudeAndLongitudeShouldBeLogged() {
//        final Latitude givenLatitude = createLatitude();
//        final Longitude givenLongitude = createLongitude();
//
//        this.failureReceivingService.receive(givenLatitude, givenLongitude);
//
//        verify(this.mockedLogger, times(1)).info(this.stringArgumentCaptor.capture());
//        assertEquals(
//                "Address wasn't received by 'FailureReceivingService'",
//                this.stringArgumentCaptor.getValue()
//        );
//    }
//
//    private static Latitude createLatitude() {
//        return Latitude.builder().build();
//    }
//
//    private static Longitude createLongitude() {
//        return Longitude.builder().build();
//    }
//
//    @Service
//    public static class SuccessfullyReceivingService implements GeocodingService {
//
//        @Override
//        public Optional<Address> receive(final double latitude, final double longitude) {
//            final Address address = Address.builder().build();
//            return Optional.of(address);
//        }
//
//    }
//
//    @Service
//    public static class FailureReceivingService implements GeocodingService {
//
//        @Override
//        public Optional<Address> receive(final double latitude, final double longitude) {
//            return empty();
//        }
//
//    }
//}
