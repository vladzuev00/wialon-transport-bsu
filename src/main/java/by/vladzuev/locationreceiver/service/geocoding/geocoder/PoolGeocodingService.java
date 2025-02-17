package by.vladzuev.locationreceiver.service.geocoding.geocoder;

import by.vladzuev.locationreceiver.crud.dto.Address;
import by.vladzuev.locationreceiver.crud.service.AddressService;
import by.vladzuev.locationreceiver.model.GpsCoordinate;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Order(1)
@RequiredArgsConstructor
public class PoolGeocodingService implements Geocoder {
    private final AddressService addressService;

    @Override
    public Optional<Address> geocode(final GpsCoordinate coordinate) {
        return addressService.findByGpsCoordinates(coordinate);
    }
}
