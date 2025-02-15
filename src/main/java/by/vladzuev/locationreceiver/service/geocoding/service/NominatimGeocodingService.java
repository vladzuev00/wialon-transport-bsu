package by.vladzuev.locationreceiver.service.geocoding.service;

import by.vladzuev.locationreceiver.crud.dto.Address;
import by.vladzuev.locationreceiver.crud.service.AddressService;
import by.vladzuev.locationreceiver.model.GpsCoordinate;
import by.vladzuev.locationreceiver.service.nominatim.NominatimService;
import by.vladzuev.locationreceiver.service.nominatim.mapper.ReverseResponseMapper;
import by.vladzuev.locationreceiver.service.nominatim.model.NominatimReverseResponse;
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
    public Optional<Address> receive(final GpsCoordinate coordinate) {
        return nominatimService.reverse(coordinate).map(this::mapToPossiblySavedAddress);
    }

    private Address mapToPossiblySavedAddress(final NominatimReverseResponse response) {
        final Address responseAddress = responseMapper.map(response);
        return addressService.findByGeometry(responseAddress.getGeometry()).orElse(responseAddress);
    }
}
