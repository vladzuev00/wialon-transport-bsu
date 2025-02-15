package by.vladzuev.locationreceiver.service.geocoding;

import by.vladzuev.locationreceiver.crud.dto.Address;
import by.vladzuev.locationreceiver.crud.service.AddressService;
import by.vladzuev.locationreceiver.model.GpsCoordinate;
import by.vladzuev.locationreceiver.service.geocoding.service.GeocodingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.function.Function.identity;

@Service
@RequiredArgsConstructor
public final class GeocodingManager {
    private final List<GeocodingService> geocodingServices;
    private final AddressService addressService;

    //TODO: возможно здесь стоит возвращать просто адрес
    public Optional<Address> findSavedAddress(final GpsCoordinate coordinate) {
        return geocodingServices.stream()
                .map(geocodingService -> geocodingService.receive(coordinate))
                .filter(Optional::isPresent)
                .findFirst()
                .flatMap(identity())
                .map(this::mapToSavedAddress);
    }

    private Address mapToSavedAddress(final Address address) {
        return address.isNew() ? addressService.save(address) : address;
    }
}
