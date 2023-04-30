package by.bsu.wialontransport.crud.entity;

import lombok.*;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "cities")
@NoArgsConstructor
@Setter
@Getter
@ToString
public class CityEntity extends AddressEntity {

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "searching_cities_process_id")
    @ToString.Exclude
    private SearchingCitiesProcessEntity searchingCitiesProcess;

    @Builder(builderMethodName = "cityBuilder")
    public CityEntity(final Long id,
                      final Geometry boundingBox,
                      final Point center,
                      final String cityName,
                      final String countryName,
                      final Geometry geometry,
                      final SearchingCitiesProcessEntity searchingCitiesProcess) {
        super(id, boundingBox, center, cityName, countryName, geometry);
        this.searchingCitiesProcess = searchingCitiesProcess;
    }
}
