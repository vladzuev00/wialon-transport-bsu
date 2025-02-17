package by.vladzuev.locationreceiver.service.geocoding.geocoder;

import by.vladzuev.locationreceiver.crud.dto.Address;
import by.vladzuev.locationreceiver.model.GpsCoordinate;
import by.vladzuev.locationreceiver.service.nominatim.NominatimClient;
import by.vladzuev.locationreceiver.service.nominatim.mapper.ReverseResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Order(2)
@Component
@RequiredArgsConstructor
public class NominatimGeocoder implements Geocoder {
    private final NominatimClient client;
    private final ReverseResponseMapper mapper;

    @Override
    public Optional<Address> geocode(final GpsCoordinate coordinate) {
        return client.reverse(coordinate).map(mapper::map);
    }
}
