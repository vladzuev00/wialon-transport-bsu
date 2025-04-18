package by.vladzuev.locationreceiver.service.geocoding;

import by.vladzuev.locationreceiver.crud.dto.Address;
import by.vladzuev.locationreceiver.model.GpsCoordinate;
import by.vladzuev.locationreceiver.service.geocoding.geocoder.Geocoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.function.Function.identity;

@Service
@RequiredArgsConstructor
public final class GeocodingService {
    private final List<Geocoder> geocoders;

    public Optional<Address> geocode(final GpsCoordinate coordinate) {
        return geocoders.stream()
                .map(geocoder -> geocoder.geocode(coordinate))
                .filter(Optional::isPresent)
                .findFirst()
                .flatMap(identity());
    }
}
