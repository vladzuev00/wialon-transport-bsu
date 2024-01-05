package by.bsu.wialontransport.controller.searchingcities.mapper;

import by.bsu.wialontransport.controller.searchingcities.model.SearchingCitiesProcessResponse;
import by.bsu.wialontransport.crud.dto.SearchingCitiesProcess;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.wololo.geojson.Geometry;
import org.wololo.jts2geojson.GeoJSONWriter;

@Component
@RequiredArgsConstructor
public final class SearchingCitiesProcessControllerMapper {
    private final GeoJSONWriter geoJSONWriter;

    public SearchingCitiesProcessResponse mapToResponse(final SearchingCitiesProcess process) {
        return SearchingCitiesProcessResponse.builder()
                .id(process.getId())
                .bounds(mapBounds(process))
                .searchStep(process.getSearchStep())
                .totalPoints(process.getTotalPoints())
                .handledPoints(process.getHandledPoints())
                .status(process.getStatus())
                .build();
    }

    private Geometry mapBounds(final SearchingCitiesProcess source) {
        return geoJSONWriter.write(source.getBounds());
    }
}
