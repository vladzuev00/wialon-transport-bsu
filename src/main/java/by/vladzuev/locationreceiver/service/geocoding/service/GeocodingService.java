package by.vladzuev.locationreceiver.service.geocoding.service;

import by.vladzuev.locationreceiver.crud.dto.Address;
import by.vladzuev.locationreceiver.model.GpsCoordinate;

import java.util.Optional;

@FunctionalInterface
public interface GeocodingService {

    Optional<Address> receive(final GpsCoordinate coordinate);

    default String findName() {
        return getClass().getSimpleName();
    }
}
