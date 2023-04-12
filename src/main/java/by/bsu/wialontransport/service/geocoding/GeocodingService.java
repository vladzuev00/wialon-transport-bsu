package by.bsu.wialontransport.service.geocoding;

import by.bsu.wialontransport.crud.dto.Address;

import java.util.Optional;

@FunctionalInterface
public interface GeocodingService {
    Optional<Address> receive(final double latitude, final double longitude);
}
