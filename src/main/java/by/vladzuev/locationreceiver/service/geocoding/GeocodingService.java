package by.vladzuev.locationreceiver.service.geocoding;

import by.vladzuev.locationreceiver.crud.dto.Address;
import by.vladzuev.locationreceiver.crud.service.AddressService;
import by.vladzuev.locationreceiver.model.GpsCoordinate;
import by.vladzuev.locationreceiver.service.geocoding.geocoder.Geocoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.nonNull;
import static java.util.function.Function.identity;

@Service
@RequiredArgsConstructor
public final class GeocodingService {
    private final List<Geocoder> geocoders;
    private final AddressService addressService;

    public Optional<Address> geocodeSavedAddress(final GpsCoordinate coordinate) {
        return geocoders.stream()
                .map(geocoder -> geocoder.geocode(coordinate))
                .filter(Optional::isPresent)
                .findFirst()
                .flatMap(identity())
                .map(this::saveIfNew);
    }

    private Address saveIfNew(final Address address) {
        if (nonNull(address.getId())) {
            return address;
        }
        return addressService.findByGeometry(address.getGeometry()).orElseGet(() -> addressService.save(address));
    }
}
