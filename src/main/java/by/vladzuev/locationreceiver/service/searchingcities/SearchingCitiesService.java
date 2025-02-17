package by.vladzuev.locationreceiver.service.searchingcities;

import by.vladzuev.locationreceiver.crud.dto.City;
import by.vladzuev.locationreceiver.model.GpsCoordinate;
import by.vladzuev.locationreceiver.service.nominatim.NominatimClient;
import by.vladzuev.locationreceiver.service.nominatim.mapper.ReverseResponseMapper;
import by.vladzuev.locationreceiver.service.nominatim.model.NominatimReverseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.Thread.currentThread;

@Service
@RequiredArgsConstructor
public final class SearchingCitiesService {
    private static final String REGEX_CITY_PLACE_VALUE_IN_JSON = "^(city)|(town)$";

    private final NominatimClient nominatimService;
    private final ReverseResponseMapper responseMapper;

    public List<City> findByCoordinates(final List<GpsCoordinate> coordinates) {
        return coordinates.stream()
                .map(this::reverseInterruptibly)
                .filter(SearchingCitiesService::isCity)
                .map(responseMapper::map)
                .map(City::createWithAddress)
                .toList();
    }

    private NominatimReverseResponse reverseInterruptibly(final GpsCoordinate coordinate) {
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
        final NominatimReverseResponse.ExtraTags extraTags = response.getExtraTags();
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
