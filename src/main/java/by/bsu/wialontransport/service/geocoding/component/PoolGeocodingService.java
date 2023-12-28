package by.bsu.wialontransport.service.geocoding.component;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Order(1)
@RequiredArgsConstructor
public class PoolGeocodingService implements GeocodingChainComponent {
    private final AddressService addressService;

    @Override
    public Optional<Address> receive(final double latitude, final double longitude) {
//        return this.addressService.findByGpsCoordinates(latitude, longitude);
        return null;
    }
}
