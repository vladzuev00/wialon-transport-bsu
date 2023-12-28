package by.bsu.wialontransport.service.searchingcities;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.dto.City;
import by.bsu.wialontransport.model.RequestCoordinate;
import by.bsu.wialontransport.service.nominatim.NominatimService;
import by.bsu.wialontransport.service.nominatim.mapper.ReverseResponseToAddressMapper;
import by.bsu.wialontransport.service.nominatim.model.NominatimReverseResponse;
import by.bsu.wialontransport.service.nominatim.model.NominatimReverseResponse.ExtraTags;
import by.bsu.wialontransport.service.searchingcities.exception.SearchingCitiesInterruptedException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static by.bsu.wialontransport.crud.dto.City.createWithAddress;
import static java.lang.Thread.currentThread;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class SearchingCitiesServiceTest {

    @Mock
    private NominatimService mockedNominatimService;

    @Mock
    private ReverseResponseToAddressMapper mockedResponseToAddressMapper;

    private SearchingCitiesService searchingCitiesService;

    @Before
    public void initializeSearchingCitiesService() {
        this.searchingCitiesService = new SearchingCitiesService(
                this.mockedNominatimService,
                this.mockedResponseToAddressMapper
        );
    }

    @Test
    public void citiesShouldBeFoundByCoordinates() {
        final RequestCoordinate firstGivenCoordinate = new RequestCoordinate(4.4, 5.5);
        final RequestCoordinate secondGivenCoordinate = new RequestCoordinate(6.6, 7.7);
        final RequestCoordinate thirdGivenCoordinate = new RequestCoordinate(8.8, 9.9);
        final List<RequestCoordinate> givenCoordinates = List.of(
                firstGivenCoordinate, secondGivenCoordinate, thirdGivenCoordinate
        );

        final NominatimReverseResponse firstGivenResponse = createResponse("city");
        when(this.mockedNominatimService.reverse(firstGivenCoordinate)).thenReturn(firstGivenResponse);

        final NominatimReverseResponse secondGivenResponse = createResponse("town");
        when(this.mockedNominatimService.reverse(secondGivenCoordinate)).thenReturn(secondGivenResponse);

        final NominatimReverseResponse thirdGivenResponse = createResponse("some-place");
        when(this.mockedNominatimService.reverse(thirdGivenCoordinate)).thenReturn(thirdGivenResponse);

        final Address firstAddress = createAddress(255L);
        when(this.mockedResponseToAddressMapper.map(firstGivenResponse)).thenReturn(firstAddress);

        final Address secondAddress = createAddress(256L);
        when(this.mockedResponseToAddressMapper.map(secondGivenResponse)).thenReturn(secondAddress);

        final List<City> actual = this.searchingCitiesService.findByCoordinates(givenCoordinates);
        final List<City> expected = List.of(createWithAddress(firstAddress), createWithAddress(secondAddress));
        assertEquals(expected, actual);
    }

    @Test(expected = SearchingCitiesInterruptedException.class)
    public void searchingCitiesShouldBeInterrupted() {
        final List<RequestCoordinate> givenCoordinates = List.of(
                new RequestCoordinate(4.4, 5.5)
        );

        currentThread().interrupt();
        this.searchingCitiesService.findByCoordinates(givenCoordinates);
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
