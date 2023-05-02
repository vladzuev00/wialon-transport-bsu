package by.bsu.wialontransport.service.geocoding.component;

import by.bsu.wialontransport.service.geocoding.GeocodingService;

public interface GeocodingChainComponent extends GeocodingService {

    default String findName() {
        final Class<?> componentClass = this.getClass();
        return componentClass.getSimpleName();
    }

}
