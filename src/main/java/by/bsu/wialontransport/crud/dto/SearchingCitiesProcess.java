package by.bsu.wialontransport.crud.dto;

import by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.locationtech.jts.geom.Geometry;

@Value
@AllArgsConstructor
@Builder
public class SearchingCitiesProcess implements Dto<Long> {
    Long id;
    Geometry bounds;
    double searchStep;
    long totalPoints;
    long handledPoints;
    Status status;
}
