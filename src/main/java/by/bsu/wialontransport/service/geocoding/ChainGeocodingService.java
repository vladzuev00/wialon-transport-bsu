package by.bsu.wialontransport.service.geocoding;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.service.geocoding.component.GeocodingChainComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.function.UnaryOperator.identity;

@Service
@RequiredArgsConstructor
public class ChainGeocodingService implements GeocodingService {
    private final List<GeocodingChainComponent> componentServices;

    @Override
    public Optional<Address> receive(final Coordinate coordinate) {
//        return this.componentServices.stream()
//                .map(service -> service.receive(latitude, longitude))
//                .filter(Optional::isPresent)
//                .findFirst()
//                .flatMap(identity());
        return null;
    }
}
