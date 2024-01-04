package by.bsu.wialontransport.service.searchingcities;

import by.bsu.wialontransport.crud.dto.City;
import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.service.nominatim.NominatimService;
import by.bsu.wialontransport.service.nominatim.mapper.ReverseResponseMapper;
import by.bsu.wialontransport.service.nominatim.model.NominatimReverseResponse;
import by.bsu.wialontransport.service.nominatim.model.NominatimReverseResponse.ExtraTags;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.Thread.currentThread;

@Service
@RequiredArgsConstructor
public final class SearchingCitiesService {
    private static final String REGEX_CITY_PLACE_VALUE_IN_JSON = "^(city)|(town)$";

    private final NominatimService nominatimService;
    private final ReverseResponseMapper responseMapper;

    public List<City> findByCoordinates(final List<Coordinate> coordinates) {
        return coordinates.stream()
                .map(this::reverseInterruptibly)
                .filter(SearchingCitiesService::isCity)
                .map(responseMapper::map)
                .map(City::createWithAddress)
                .toList();
    }

    private NominatimReverseResponse reverseInterruptibly(final Coordinate coordinate) {
        checkInterrupted();
        return nominatimService.reverse(coordinate)
                .orElseThrow(
                        () -> new SearchingCitiesException(
                                "There is no reverse response from Nominatim by coordinate: %s".formatted(coordinate)
                        )
                );
    }

    private static void checkInterrupted() {
        if (currentThread().isInterrupted()) {
            throw new SearchingCitiesException("Thread was interrupted");
        }
    }

    private static boolean isCity(final NominatimReverseResponse response) {
        final ExtraTags extraTags = response.getExtraTags();
        return extraTags != null
                && extraTags.getPlace() != null
                && extraTags.getPlace().matches(REGEX_CITY_PLACE_VALUE_IN_JSON);
    }

    static final class SearchingCitiesException extends RuntimeException {

        @SuppressWarnings("unused")
        public SearchingCitiesException() {

        }

        public SearchingCitiesException(final String description) {
            super(description);
        }

        @SuppressWarnings("unused")
        public SearchingCitiesException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public SearchingCitiesException(final String description, final Exception cause) {
            super(description, cause);
        }

    }

}
