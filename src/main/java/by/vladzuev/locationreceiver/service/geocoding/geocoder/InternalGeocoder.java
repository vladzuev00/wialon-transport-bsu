package by.vladzuev.locationreceiver.service.geocoding.geocoder;

import by.vladzuev.locationreceiver.crud.dto.Address;
import by.vladzuev.locationreceiver.crud.service.AddressService;
import by.vladzuev.locationreceiver.model.GpsCoordinate;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static by.vladzuev.locationreceiver.util.CoordinateUtil.jtsPoint;

@Order(1)
@Component
@RequiredArgsConstructor
public class InternalGeocoder implements Geocoder {
    private final GeometryFactory geometryFactory;
    private final AddressService addressService;

    @Override
    public Optional<Address> geocode(final GpsCoordinate coordinate) {
        final Point point = jtsPoint(geometryFactory, coordinate);
        return addressService.findByPoint(point);
    }
}
