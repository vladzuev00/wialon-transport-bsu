package by.bsu.wialontransport.controller.searchingcities.mapper;

import by.bsu.wialontransport.controller.searchingcities.model.SearchingCitiesProcessPageResponse;
import by.bsu.wialontransport.controller.searchingcities.model.SearchingCitiesProcessResponse;
import by.bsu.wialontransport.crud.dto.SearchingCitiesProcess;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.wololo.geojson.Geometry;
import org.wololo.jts2geojson.GeoJSONWriter;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
public final class SearchingCitiesProcessControllerMapper {
    private final GeoJSONWriter geoJSONWriter;

    public SearchingCitiesProcessResponse mapToResponse(final SearchingCitiesProcess mapped) {
        return SearchingCitiesProcessResponse.builder()
                .id(mapped.getId())
                .bounds(this.mapBounds(mapped))
                .searchStep(mapped.getSearchStep())
                .totalPoints(mapped.getTotalPoints())
                .handledPoints(mapped.getHandledPoints())
                .status(mapped.getStatus())
                .build();
    }

    public List<SearchingCitiesProcessResponse> mapToResponses(final List<SearchingCitiesProcess> mapped) {
        return mapped.stream()
                .map(this::mapToResponse)
                .collect(toList());
    }

    public SearchingCitiesProcessPageResponse mapToResponse(final int pageNumber,
                                                            final int pageSize,
                                                            final List<SearchingCitiesProcess> processes) {
        return SearchingCitiesProcessPageResponse.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .processes(this.mapToResponses(processes))
                .build();
    }

    private Geometry mapBounds(final SearchingCitiesProcess source) {
        return this.geoJSONWriter.write(source.getBounds());
    }
}
