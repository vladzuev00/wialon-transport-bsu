package by.bsu.wialontransport.service.geocoding.service;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.model.GpsCoordinate;

import java.util.Optional;

@FunctionalInterface
public interface GeocodingService {

    Optional<Address> receive(final GpsCoordinate coordinate);

    default String findName() {
        return getClass().getSimpleName();
    }
}
