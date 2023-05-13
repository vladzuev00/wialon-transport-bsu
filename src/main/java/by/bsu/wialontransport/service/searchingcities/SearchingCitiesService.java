package by.bsu.wialontransport.service.searchingcities;

import by.bsu.wialontransport.crud.dto.City;
import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.service.nominatim.NominatimService;
import by.bsu.wialontransport.service.nominatim.mapper.ReverseResponseToAddressMapper;
import by.bsu.wialontransport.service.nominatim.model.NominatimReverseResponse;
import by.bsu.wialontransport.service.nominatim.model.NominatimReverseResponse.ExtraTags;
import by.bsu.wialontransport.service.searchingcities.exception.SearchingCitiesInterruptedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.Thread.currentThread;

//TODO: refactor tests
@Service
@RequiredArgsConstructor
public final class SearchingCitiesService {
    private static final String REGEX_PLACE_VALUE_IN_JSON_OF_CITY = "(city)|(town)";

    private final NominatimService nominatimService;
    private final ReverseResponseToAddressMapper responseToAddressMapper;

    public List<City> findByCoordinates(final List<Coordinate> coordinates) {
        return coordinates.stream()
                .map(this::reverseWithCheckingInterrupted)
                .filter(SearchingCitiesService::isCity)
                .map(this.responseToAddressMapper::map)
                .map(City::createWithAddress)
                .toList();
    }

    private NominatimReverseResponse reverseWithCheckingInterrupted(final Coordinate coordinate) {
        checkInterrupted();
        return this.nominatimService.reverse(coordinate);
    }

    private static void checkInterrupted() {
        if (currentThread().isInterrupted()) {
            throw new SearchingCitiesInterruptedException();
        }
    }

    private static boolean isCity(final NominatimReverseResponse response) {
        final ExtraTags extraTags = response.getExtraTags();
        return extraTags != null
                && extraTags.getPlace() != null
                && extraTags.getPlace().matches(REGEX_PLACE_VALUE_IN_JSON_OF_CITY);
    }
}
