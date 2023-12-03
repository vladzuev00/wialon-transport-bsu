package by.bsu.wialontransport.crud.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.locationtech.jts.geom.Geometry;

@Value
@AllArgsConstructor
@Builder
public class City implements Dto<Long> {
    Long id;
    Address address;
    SearchingCitiesProcess searchingCitiesProcess;

    public Geometry findGeometry() {
        return this.address.getGeometry();
    }

    public static City copyWithAddressAndProcess(final City source,
                                                 final Address address,
                                                 final SearchingCitiesProcess process) {
        final Long sourceId = source.getId();
        return new City(sourceId, address, process);
    }

    public static City createWithAddress(final Address address) {
        return new City(null, address, null);
    }
}
