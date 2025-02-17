package by.vladzuev.locationreceiver.service.geocoding.geocoder;

import by.vladzuev.locationreceiver.crud.dto.Address;
import by.vladzuev.locationreceiver.model.GpsCoordinate;

import java.util.Optional;

@FunctionalInterface
public interface Geocoder {

    Optional<Address> geocode(final GpsCoordinate coordinate);
}
