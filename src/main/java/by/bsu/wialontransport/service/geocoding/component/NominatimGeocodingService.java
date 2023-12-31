package by.bsu.wialontransport.service.geocoding.component;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.service.AddressService;
import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.service.geocoding.GeocodingService;
import by.bsu.wialontransport.service.nominatim.NominatimService;
import by.bsu.wialontransport.service.nominatim.mapper.ReverseResponseToAddressMapper;
import by.bsu.wialontransport.service.nominatim.model.NominatimReverseResponse;
import org.locationtech.jts.geom.Geometry;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Order(2)
public class NominatimGeocodingService implements GeocodingService {
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
    public Optional<Address> receive(final Coordinate coordinate) {
//        final NominatimReverseResponse response = this.nominatimService.reverse(latitude, longitude);
//        return response != null
//                ? Optional.of(this.mapToPossiblySavedAddress(response))
//                : empty();
        return null;
        //TODO: return not saved address
    }

    private Address mapToPossiblySavedAddress(final NominatimReverseResponse response) {
        final Address responseAddress = this.responseToAddressMapper.map(response);
        final Geometry responseAddressGeometry = responseAddress.getGeometry();
        final Optional<Address> optionalSavedAddressWithSameGeometry = this.addressService.findByGeometry(
                responseAddressGeometry
        );
        return optionalSavedAddressWithSameGeometry.orElse(responseAddress);
    }
}
