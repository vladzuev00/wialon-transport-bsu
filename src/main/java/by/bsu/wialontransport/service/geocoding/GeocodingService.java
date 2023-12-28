package by.bsu.wialontransport.service.geocoding;

import by.bsu.wialontransport.crud.dto.Address;

import java.util.Optional;

@FunctionalInterface
public interface GeocodingService {

    Optional<Address> receive(final double latitude, final double longitude);

//    default Optional<Address> receive(final Latitude latitude, final Longitude longitude) {
//        final double latitudeDoubleValue = latitude.findDoubleValue();
//        final double longitudeDoubleValue = longitude.findDoubleValue();
//        return this.receive(latitudeDoubleValue, longitudeDoubleValue);
//    }

    default String findName() {
        final Class<?> componentClass = this.getClass();
        return componentClass.getSimpleName();
    }

}
