package by.bsu.wialontransport.service.geocoding.service;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.service.AddressService;
import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.service.nominatim.NominatimService;
import by.bsu.wialontransport.service.nominatim.mapper.ReverseResponseMapper;
import by.bsu.wialontransport.service.nominatim.model.NominatimReverseResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Order(2)
public class NominatimGeocodingService implements GeocodingService {
    private final NominatimService nominatimService;
    private final ReverseResponseMapper responseMapper;
    private final AddressService addressService;

    public NominatimGeocodingService(final NominatimService nominatimService,
                                     final ReverseResponseMapper responseMapper,
                                     final AddressService addressService) {
        this.nominatimService = nominatimService;
        this.responseMapper = responseMapper;
        this.addressService = addressService;
    }

    @Override
    public Optional<Address> receive(final Coordinate coordinate) {
        return nominatimService.reverse(coordinate).map(this::mapToPossiblySavedAddress);
    }

    private Address mapToPossiblySavedAddress(final NominatimReverseResponse response) {
        final Address responseAddress = responseMapper.map(response);
        return addressService.findByGeometry(responseAddress.getGeometry()).orElse(responseAddress);
    }
}
