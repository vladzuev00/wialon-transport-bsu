package by.bsu.wialontransport.crud.dto;

import lombok.*;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class City extends Address {
    private final SearchingCitiesProcess searchingCitiesProcess;

    @Builder(builderMethodName = "cityBuilder")
    public City(final Long id,
                final Geometry boundingBox,
                final Point center,
                final String cityName,
                final String countryName,
                final Geometry geometry,
                final SearchingCitiesProcess searchingCitiesProcess) {
        super(id, boundingBox, center, cityName, countryName, geometry);
        this.searchingCitiesProcess = searchingCitiesProcess;
    }
}
