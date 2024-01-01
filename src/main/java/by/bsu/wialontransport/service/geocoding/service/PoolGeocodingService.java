package by.bsu.wialontransport.service.geocoding.service;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.service.AddressService;
import by.bsu.wialontransport.model.Coordinate;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Order(1)
@RequiredArgsConstructor
public class PoolGeocodingService implements GeocodingService {
    private final AddressService addressService;

    @Override
    public Optional<Address> receive(final Coordinate coordinate) {
//        return this.addressService.findByGpsCoordinates(latitude, longitude);
        return null;
    }
}
