package by.bsu.wialontransport.crud.dto;

import by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity.Status;

import lombok.Value;
import org.locationtech.jts.geom.Geometry;

@Value
public class SearchingCitiesProcess {
    Long id;
    Geometry bounds;
    double searchStep;
    long totalPoints;
    long handledPoints;
    Status status;
}
