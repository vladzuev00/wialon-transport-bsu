package by.vladzuev.locationreceiver.service.geocoding.geocoder;

import by.vladzuev.locationreceiver.crud.dto.Address;
import by.vladzuev.locationreceiver.crud.service.AddressService;
import by.vladzuev.locationreceiver.model.GpsCoordinate;
import by.vladzuev.locationreceiver.util.CoordinateUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static by.vladzuev.locationreceiver.util.CoordinateUtil.jtsPoint;
import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class InternalGeocoderTest {

    @Mock
    private GeometryFactory mockedGeometryFactory;

    @Mock
    private AddressService mockedAddressService;

    private InternalGeocoder geocoder;

    @BeforeEach
    public void initializeGeocoder() {
        geocoder = new InternalGeocoder(mockedGeometryFactory, mockedAddressService);
    }

    @Test
    public void addressShouldBeGeocoded() {
        try (final MockedStatic<CoordinateUtil> mockedUtil = mockStatic(CoordinateUtil.class)) {
            final GpsCoordinate givenCoordinate = GpsCoordinate.builder().build();

            final Point givenPoint = mock(Point.class);
            mockedUtil.when(() -> jtsPoint(same(mockedGeometryFactory), same(givenCoordinate))).thenReturn(givenPoint);

            final Address givenAddress = Address.builder().build();
            when(mockedAddressService.findByPoint(same(givenPoint))).thenReturn(Optional.of(givenAddress));

            final Optional<Address> optionalActual = geocoder.geocode(givenCoordinate);
            assertTrue(optionalActual.isPresent());
            final Address actual = optionalActual.get();
            assertSame(givenAddress, actual);
        }
    }

    @Test
    public void addressShouldNotBeGeocoded() {
        try (final MockedStatic<CoordinateUtil> mockedUtil = mockStatic(CoordinateUtil.class)) {
            final GpsCoordinate givenCoordinate = GpsCoordinate.builder().build();

            final Point givenPoint = mock(Point.class);
            mockedUtil.when(() -> jtsPoint(same(mockedGeometryFactory), same(givenCoordinate))).thenReturn(givenPoint);

            when(mockedAddressService.findByPoint(same(givenPoint))).thenReturn(empty());

            final Optional<Address> optionalActual = geocoder.geocode(givenCoordinate);
            assertTrue(optionalActual.isEmpty());
        }
    }
}
