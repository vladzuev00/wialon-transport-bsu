package by.bsu.wialontransport.service.geocoding.component;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.service.AddressService;
import by.bsu.wialontransport.service.nominatim.NominatimService;
import by.bsu.wialontransport.service.nominatim.mapper.ReverseResponseToAddressMapper;
import by.bsu.wialontransport.service.nominatim.model.NominatimReverseResponse;
import org.locationtech.jts.geom.Geometry;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Optional.empty;

@Service
@Order(2)
public class NominatimGeocodingService implements GeocodingChainComponent {
    private final NominatimService nominatimService;
    private final ReverseResponseToAddressMapper responseToAddressMapper;
    private final AddressService addressService;

    public NominatimGeocodingService(final NominatimService nominatimService,
                                     final ReverseResponseToAddressMapper responseToAddressMapper,
                                     final AddressService addressService) {
        this.nominatimService = nominatimService;
        this.responseToAddressMapper = responseToAddressMapper;
        this.addressService = addressService;
    }

    @Override
    public Optional<Address> receive(final double latitude, final double longitude) {
        final NominatimReverseResponse response = this.nominatimService.reverse(latitude, longitude);
        return response != null
                ? Optional.of(this.mapToPossiblySavedAddress(response))
                : empty();
    }

    private Address mapToPossiblySavedAddress(final NominatimReverseResponse response) {
        final Address responseAddress = this.responseToAddressMapper.map(response);
        final Geometry responseAddressGeometry = responseAddress.getGeometry();
        final Optional<Address> optionalSavedAddressWithSameGeometry = this.addressService.findAddressByGeometry(
                responseAddressGeometry
        );
        return optionalSavedAddressWithSameGeometry.orElse(responseAddress);
    }
}