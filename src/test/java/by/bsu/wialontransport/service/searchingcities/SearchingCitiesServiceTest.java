package by.bsu.wialontransport.service.searchingcities;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.dto.City;
import by.bsu.wialontransport.model.GpsCoordinate;
import by.bsu.wialontransport.service.nominatim.NominatimService;
import by.bsu.wialontransport.service.nominatim.mapper.ReverseResponseMapper;
import by.bsu.wialontransport.service.nominatim.model.NominatimReverseResponse;
import by.bsu.wialontransport.service.nominatim.model.NominatimReverseResponse.ExtraTags;
import by.bsu.wialontransport.service.searchingcities.SearchingCitiesService.SearchingCitiesException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static by.bsu.wialontransport.crud.dto.City.createWithAddress;
import static java.lang.Thread.currentThread;
import static java.util.Collections.singletonList;
import static java.util.Optional.empty;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class SearchingCitiesServiceTest {

    @Mock
    private NominatimService mockedNominatimService;

    @Mock
    private ReverseResponseMapper mockedResponseMapper;

    private SearchingCitiesService searchingCitiesService;

    @Before
    public void initializeSearchingCitiesService() {
        searchingCitiesService = new SearchingCitiesService(mockedNominatimService, mockedResponseMapper);
    }

    @Test
    public void citiesShouldBeFoundByCoordinates() {
        final GpsCoordinate firstGivenCoordinate = new GpsCoordinate(4.4, 5.5);
        final GpsCoordinate secondGivenCoordinate = new GpsCoordinate(6.6, 7.7);
        final GpsCoordinate thirdGivenCoordinate = new GpsCoordinate(8.8, 9.9);
        final List<GpsCoordinate> givenCoordinates = List.of(
                firstGivenCoordinate,
                secondGivenCoordinate,
                thirdGivenCoordinate
        );

        final NominatimReverseResponse firstGivenResponse = createResponse("city");
        when(mockedNominatimService.reverse(same(firstGivenCoordinate))).thenReturn(Optional.of(firstGivenResponse));

        final NominatimReverseResponse secondGivenResponse = createResponse("town");
        when(mockedNominatimService.reverse(same(secondGivenCoordinate))).thenReturn(Optional.of(secondGivenResponse));

        final NominatimReverseResponse thirdGivenResponse = createResponse("some-place");
        when(mockedNominatimService.reverse(same(thirdGivenCoordinate))).thenReturn(Optional.of(thirdGivenResponse));

        final Address firstAddress = createAddress(255L);
        when(mockedResponseMapper.map(same(firstGivenResponse))).thenReturn(firstAddress);

        final Address secondAddress = createAddress(256L);
        when(mockedResponseMapper.map(same(secondGivenResponse))).thenReturn(secondAddress);

        final List<City> actual = searchingCitiesService.findByCoordinates(givenCoordinates);
        final List<City> expected = List.of(
                createWithAddress(firstAddress),
                createWithAddress(secondAddress)
        );
        assertEquals(expected, actual);
    }

    @Test(expected = SearchingCitiesException.class)
    public void searchingCitiesShouldBeInterrupted() {
        final List<GpsCoordinate> givenCoordinates = List.of(new GpsCoordinate(4.4, 5.5));

        currentThread().interrupt();

        searchingCitiesService.findByCoordinates(givenCoordinates);
    }

    @Test(expected = SearchingCitiesException.class)
    public void citiesShouldNotBeFoundByCoordinatesBecauseOfThereIsNoResponseFromNominatim() {
        final GpsCoordinate givenCoordinate = new GpsCoordinate(4.4, 5.5);
        final List<GpsCoordinate> givenCoordinates = singletonList(givenCoordinate);

        when(mockedNominatimService.reverse(same(givenCoordinate))).thenReturn(empty());

        searchingCitiesService.findByCoordinates(givenCoordinates);
    }

    private static NominatimReverseResponse createResponse(final String place) {
        return NominatimReverseResponse.builder()
                .extraTags(createExtraTags(place))
                .build();
    }

    private static ExtraTags createExtraTags(final String place) {
        return ExtraTags.builder()
                .place(place)
                .build();
    }

    private static Address createAddress(final Long id) {
        return Address.builder()
                .id(id)
                .build();
    }
}
