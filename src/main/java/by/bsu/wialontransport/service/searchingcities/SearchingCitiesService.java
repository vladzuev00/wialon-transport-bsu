package by.bsu.wialontransport.service.searchingcities;

import by.bsu.wialontransport.crud.dto.City;
import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.service.nominatim.NominatimService;
import by.bsu.wialontransport.service.nominatim.mapper.ReverseResponseToCityMapper;
import by.bsu.wialontransport.service.nominatim.model.NominatimReverseResponse;
import by.bsu.wialontransport.service.nominatim.model.NominatimReverseResponse.ExtraTags;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public final class SearchingCitiesService {
    private static final String REGEX_PLACE_VALUE_IN_JSON_OF_CITY = "(city)|(town)";

    private final NominatimService nominatimService;
    private final ReverseResponseToCityMapper mapper;

    public Collection<City> findByCoordinates(final List<Coordinate> coordinates) {
        return coordinates.stream()
                .map(this.nominatimService::reverse)
                .filter(SearchingCitiesService::isCity)
                .map(this.mapper::map)
                .collect(toList());
    }

    private static boolean isCity(final NominatimReverseResponse response) {
        final ExtraTags extraTags = response.getExtraTags();
        return extraTags != null
                && extraTags.getPlace() != null
                && extraTags.getPlace().matches(REGEX_PLACE_VALUE_IN_JSON_OF_CITY);
    }
}
