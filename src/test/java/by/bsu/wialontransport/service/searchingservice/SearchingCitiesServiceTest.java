package by.bsu.wialontransport.service.searchingservice;

import by.bsu.wialontransport.service.nominatim.NominatimService;
import by.bsu.wialontransport.service.nominatim.mapper.ReverseResponseToCityMapper;
import by.bsu.wialontransport.service.searchingcities.SearchingCitiesService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public final class SearchingCitiesServiceTest {

    @Mock
    private NominatimService mockedNominatimService;

    @Mock
    private ReverseResponseToCityMapper mockedResponseToCityMapper;

    private SearchingCitiesService searchingCitiesService;

    @Before
    public void initializeSearchingCitiesService() {
        this.searchingCitiesService = new SearchingCitiesService(
                this.mockedNominatimService,
                this.mockedResponseToCityMapper
        );
    }

    @Test
    public void citiesShouldBeFoundByCoordinates() {
        throw new RuntimeException();
    }
}
