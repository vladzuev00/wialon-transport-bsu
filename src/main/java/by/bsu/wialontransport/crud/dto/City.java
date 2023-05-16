package by.bsu.wialontransport.crud.dto;

import lombok.*;
import org.locationtech.jts.geom.Geometry;

@Value
@AllArgsConstructor
@Builder
public class City implements AbstractDto<Long> {
    Long id;
    Address address;
    SearchingCitiesProcess searchingCitiesProcess;

    public Geometry getGeometry() {
        return this.address.getGeometry();
    }

    public String getCityName() {
        return this.address.getCityName();
    }

    public static City copyWithAddressAndProcess(final City source,
                                                 final Address address,
                                                 final SearchingCitiesProcess process) {
        return new City(
                source.getId(),
                address,
                process
        );
    }

    public static City createWithAddress(final Address address) {
        return new City(null, address, null);
    }
}
