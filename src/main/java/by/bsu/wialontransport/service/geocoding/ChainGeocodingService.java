package by.bsu.wialontransport.service.geocoding;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.service.geocoding.component.GeocodingChainComponent;
import by.bsu.wialontransport.service.geocoding.exception.GeocodingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public final class ChainGeocodingService implements GeocodingService {
    private final List<GeocodingChainComponent> componentServices;

    @Override
    public Optional<Address> receive(final double latitude, final double longitude) {
        return this.componentServices.stream()
                .map(service -> service.receive(latitude, longitude))
                .filter(Optional::isPresent)
                .findFirst()
                .orElseThrow(() -> new GeocodingException("There are no geocoding services."));
    }
}
