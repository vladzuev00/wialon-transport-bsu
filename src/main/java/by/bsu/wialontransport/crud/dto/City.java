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

    public static City createWithAddressAndProcess(final City source,
                                                   final Address address,
                                                   final SearchingCitiesProcess process) {
        return new City(
                source.getId(),
                address,
                process
        );
    }

    //TODO: test
    public static City createWithSearchingCitiesProcess(final City source, final SearchingCitiesProcess process) {
        return new City(
                source.getId(),
                source.getAddress(),
                process
        );
    }
}
