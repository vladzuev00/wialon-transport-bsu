package by.bsu.wialontransport.controller.searchingcities;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.SearchingCitiesProcess;
import by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity.Status;
import by.bsu.wialontransport.crud.service.SearchingCitiesProcessService;
import org.junit.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity.Status.HANDLING;
import static by.bsu.wialontransport.util.GeometryUtil.createPolygon;
import static java.util.Optional.empty;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public final class SearchingCitiesProcessControllerTest extends AbstractContextTest {
    private static final String CONTROLLER_URL = "/searchCities";

    @MockBean
    private SearchingCitiesProcessService mockedProcessService;

    @Autowired
    private GeometryFactory geometryFactory;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void processShouldBeFoundById() {
        final Long givenId = 255L;
        final SearchingCitiesProcess givenProcess = SearchingCitiesProcess.builder()
                .id(givenId)
                .bounds(createPolygon(this.geometryFactory, 1, 1, 1, 2, 2, 2, 2, 1))
                .searchStep(0.5)
                .totalPoints(100)
                .handledPoints(0)
                .status(HANDLING)
                .build();

        when(this.mockedProcessService.findById(givenId)).thenReturn(Optional.of(givenProcess));

        final String url = createUrlToFindProcessById(givenId);
        final ResponseEntity<String> responseEntity = this.restTemplate.getForEntity(url, String.class);

        assertSame(OK, responseEntity.getStatusCode());

        final String actual = responseEntity.getBody();
        final String expected = "{\"id\":255,\"bounds\":{\"type\":\"Polygon\","
                + "\"coordinates\":[[[1.0,1.0],[1.0,2.0],[2.0,2.0],[2.0,1.0],[1.0,1.0]]]},"
                + "\"searchStep\":0.5,\"totalPoints\":100,\"handledPoints\":0,\"status\":\"HANDLING\"}";
        assertEquals(expected, actual);
    }

    @Test
    public void processShouldNotBeFoundById() {
        final Long givenId = 255L;

        when(this.mockedProcessService.findById(givenId)).thenReturn(empty());

        final String url = createUrlToFindProcessById(givenId);
        final ResponseEntity<String> responseEntity = this.restTemplate.getForEntity(url, String.class);

        assertSame(NOT_FOUND, responseEntity.getStatusCode());

        final String actual = responseEntity.getBody();
        final String expectedRegex = "\\{\"httpStatus\":\"NOT_FOUND\",\"message\":\"Process with id '255' doesn't exist\\.\","
                + "\"dateTime\":\"\\d{4}-\\d{2}-\\d{2} \\d{2}-\\d{2}-\\d{2}\"}";
        assertNotNull(actual);
        assertTrue(actual.matches(expectedRegex));
    }

    @Test
    public void processesShouldBeFoundByStatuses() {
        final SearchingCitiesProcess firstGivenProcess = SearchingCitiesProcess.builder()
                .id(255L)
                .bounds(createPolygon(this.geometryFactory, 1, 1, 1, 2, 2, 2, 2, 1))
                .searchStep(0.5)
                .totalPoints(100)
                .handledPoints(0)
                .status(HANDLING)
                .build();
        final SearchingCitiesProcess secondGivenProcess = SearchingCitiesProcess.builder()
                .id(256L)
                .bounds(createPolygon(this.geometryFactory, 1, 1, 1, 2, 2, 2, 2, 1))
                .searchStep(0.5)
                .totalPoints(100)
                .handledPoints(0)
                .status(HANDLING)
                .build();
        final List<SearchingCitiesProcess> givenProcesses = List.of(firstGivenProcess, secondGivenProcess);

        final Status givenStatus = HANDLING;
        final int givenPageNumber = 0;
        final int givenPageSize = 2;
        when(this.mockedProcessService.findByStatus(givenStatus, givenPageNumber, givenPageSize))
                .thenReturn(givenProcesses);


    }

    private static String createUrlToFindProcessById(final Long id) {
        return CONTROLLER_URL + "/" + id;
    }

    private static String createUrlToFindProcessesByStatus() {

    }
}
