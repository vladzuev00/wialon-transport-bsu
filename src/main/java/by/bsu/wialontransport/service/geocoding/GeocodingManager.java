package by.bsu.wialontransport.service.geocoding;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.service.AddressService;
import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.service.geocoding.service.GeocodingService;
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
    public Optional<Address> findSavedAddress(final Coordinate coordinate) {
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
