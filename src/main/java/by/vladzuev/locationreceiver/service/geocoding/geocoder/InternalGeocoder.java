package by.vladzuev.locationreceiver.service.geocoding.geocoder;

import by.vladzuev.locationreceiver.crud.dto.Address;
import by.vladzuev.locationreceiver.crud.service.AddressService;
import by.vladzuev.locationreceiver.model.GpsCoordinate;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Order(1)
@Component
@RequiredArgsConstructor
public class InternalGeocoder implements Geocoder {
    private final AddressService addressService;

    @Override
    public Optional<Address> geocode(final GpsCoordinate coordinate) {
        return addressService.findByCoordinate(coordinate);
    }
}
