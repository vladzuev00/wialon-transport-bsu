//package by.bsu.wialontransport.service.geocoding;
//
//import by.bsu.wialontransport.crud.dto.Address;
//import by.bsu.wialontransport.service.geocoding.component.GeocodingChainComponent;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.util.Optional;
//
//import static java.util.Arrays.asList;
//import static java.util.Optional.empty;
//import static org.junit.Assert.assertSame;
//import static org.junit.Assert.assertTrue;
//import static org.mockito.Mockito.*;
//
//@RunWith(MockitoJUnitRunner.class)
//public final class ChainGeocodingServiceTest {
//
//    @Mock
//    private GeocodingChainComponent firstMockedComponent;
//
//    @Mock
//    private GeocodingChainComponent secondMockedComponent;
//
//    @Test
//    public void addressShouldBeFoundByFirstComponent() {
//        final ChainGeocodingService givenChainGeocodingService = createChainGeocodingService(
//                this.firstMockedComponent, this.secondMockedComponent
//        );
//        final double givenLatitude = 4.4;
//        final double givenLongitude = 5.5;
//
//        final Address givenAddress = createAddress();
//        when(this.firstMockedComponent.receive(givenLatitude, givenLongitude))
//                .thenReturn(Optional.of(givenAddress));
//
//        final Optional<Address> optionalActual = givenChainGeocodingService.receive(givenLatitude, givenLongitude);
//        assertTrue(optionalActual.isPresent());
//        final Address actual = optionalActual.get();
//
//        assertSame(givenAddress, actual);
//
//        verify(this.firstMockedComponent, times(1))
//                .receive(givenLatitude, givenLongitude);
//        verify(this.secondMockedComponent, times(0))
//                .receive(anyDouble(), anyDouble());
//    }
//
//    @Test
//    public void addressShouldBeFoundBySecondComponent() {
//        final ChainGeocodingService givenChainGeocodingService = createChainGeocodingService(
//                this.firstMockedComponent, this.secondMockedComponent
//        );
//        final double givenLatitude = 4.4;
//        final double givenLongitude = 5.5;
//
//        when(this.firstMockedComponent.receive(givenLatitude, givenLongitude))
//                .thenReturn(empty());
//
//        final Address givenAddress = createAddress();
//        when(this.secondMockedComponent.receive(givenLatitude, givenLongitude))
//                .thenReturn(Optional.of(givenAddress));
//
//        final Optional<Address> optionalActual = givenChainGeocodingService.receive(givenLatitude, givenLongitude);
//        assertTrue(optionalActual.isPresent());
//        final Address actual = optionalActual.get();
//
//        assertSame(givenAddress, actual);
//
//        verify(this.firstMockedComponent, times(1))
//                .receive(givenLatitude, givenLongitude);
//        verify(this.secondMockedComponent, times(1))
//                .receive(givenLatitude, givenLongitude);
//    }
//
//    @Test
//    public void addressShouldNotBeFound() {
//        final ChainGeocodingService givenChainGeocodingService = createChainGeocodingService(
//                this.firstMockedComponent, this.secondMockedComponent
//        );
//        final double givenLatitude = 4.4;
//        final double givenLongitude = 5.5;
//
//        when(this.firstMockedComponent.receive(givenLatitude, givenLongitude))
//                .thenReturn(empty());
//        when(this.secondMockedComponent.receive(givenLatitude, givenLongitude))
//                .thenReturn(empty());
//
//        final Optional<Address> optionalActual = givenChainGeocodingService.receive(givenLatitude, givenLongitude);
//        assertTrue(optionalActual.isEmpty());
//
//        verify(this.firstMockedComponent, times(1))
//                .receive(givenLatitude, givenLongitude);
//        verify(this.secondMockedComponent, times(1))
//                .receive(givenLatitude, givenLongitude);
//    }
//
//    @Test
//    public void addressShouldNotBeFoundBecauseOfThereAreNoComponents() {
//        final ChainGeocodingService givenChainGeocodingService = createChainGeocodingService();
//        final double givenLatitude = 4.4;
//        final double givenLongitude = 5.5;
//
//        final Optional<Address> optionalActual = givenChainGeocodingService.receive(givenLatitude, givenLongitude);
//        assertTrue(optionalActual.isEmpty());
//    }
//
//    private static ChainGeocodingService createChainGeocodingService(final GeocodingChainComponent... components) {
//        return new ChainGeocodingService(asList(components));
//    }
//
//    private static Address createAddress() {
//        return Address.builder().build();
//    }
//}
