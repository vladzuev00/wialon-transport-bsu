package by.bsu.wialontransport.controller.searchingcities;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.SearchingCitiesProcess;
import by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity.Status;
import by.bsu.wialontransport.crud.service.SearchingCitiesProcessService;
import by.bsu.wialontransport.model.AreaCoordinate;
import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.service.searchingcities.StartingSearchingCitiesProcessService;
import org.junit.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

import static by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity.Status.HANDLING;
import static by.bsu.wialontransport.util.GeometryUtil.createPolygon;
import static java.util.Optional.empty;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public final class SearchingCitiesProcessControllerTest extends AbstractContextTest {
    private static final String CONTROLLER_URL = "/searchCities";

    private static final String PARAM_NAME_STATUS = "status";
    private static final String PARAM_NAME_PAGE_NUMBER = "pageNumber";
    private static final String PARAM_NAME_PAGE_SIZE = "pageSize";

    @MockBean
    private SearchingCitiesProcessService mockedProcessService;

    @MockBean
    private StartingSearchingCitiesProcessService mockedStartingProcessService;

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

        final String url = createUrlToFindProcessesByStatus(givenStatus, givenPageNumber, givenPageSize);
        final ResponseEntity<String> responseEntity = this.restTemplate.getForEntity(url, String.class);

        assertSame(OK, responseEntity.getStatusCode());

        final String actual = responseEntity.getBody();
        final String expected = "{\"pageNumber\":0,\"pageSize\":2,"
                + "\"processes\":[{\"id\":255,\"bounds\":{\"type\":\"Polygon\","
                + "\"coordinates\":[[[1.0,1.0],[1.0,2.0],[2.0,2.0],[2.0,1.0],[1.0,1.0]]]},"
                + "\"searchStep\":0.5,\"totalPoints\":100,\"handledPoints\":0,\"status\":\"HANDLING\"},"
                + "{\"id\":256,\"bounds\":{\"type\":\"Polygon\","
                + "\"coordinates\":[[[1.0,1.0],[1.0,2.0],[2.0,2.0],[2.0,1.0],[1.0,1.0]]]},"
                + "\"searchStep\":0.5,\"totalPoints\":100,\"handledPoints\":0,\"status\":\"HANDLING\"}]}";
        assertEquals(expected, actual);
    }

    @Test
    public void processesShouldNotBeFoundBecauseOfStatusIsNotDefined() {
        final int givenPageNumber = 0;
        final int givenPageSize = 2;

        final String url = createUrlToFindProcessesByStatus((Status) null, givenPageNumber, givenPageSize);
        final ResponseEntity<String> responseEntity = this.restTemplate.getForEntity(url, String.class);

        assertSame(NOT_ACCEPTABLE, responseEntity.getStatusCode());

        final String actual = responseEntity.getBody();
        final String expectedRegex = "\\{\"httpStatus\":\"NOT_ACCEPTABLE\","
                + "\"message\":\"Required request parameter 'status' for method parameter type Status "
                + "is not present\","
                + "\"dateTime\":\"\\d{4}-\\d{2}-\\d{2} \\d{2}-\\d{2}-\\d{2}\"}";
        assertNotNull(actual);
        assertTrue(actual.matches(expectedRegex));
    }

    @Test
    public void processesShouldNotBeFoundBecauseOfNotValidStatus() {
        final int givenPageNumber = 0;
        final int givenPageSize = 2;

        final String url = createUrlToFindProcessesByStatus(
                "some-status", givenPageNumber, givenPageSize
        );
        final ResponseEntity<String> responseEntity = this.restTemplate.getForEntity(url, String.class);

        assertSame(NOT_ACCEPTABLE, responseEntity.getStatusCode());

        final String actual = responseEntity.getBody();
        final String expectedRegex = "\\{\"httpStatus\":\"NOT_ACCEPTABLE\","
                + "\"message\":\"'some-status' should be replaced by one of: HANDLING, SUCCESS, ERROR\\.\","
                + "\"dateTime\":\"\\d{4}-\\d{2}-\\d{2} \\d{2}-\\d{2}-\\d{2}\"}";
        assertNotNull(actual);
        assertTrue(actual.matches(expectedRegex));
    }

    @Test
    public void processesShouldNotBeFoundBecauseOfPageNumberIsNotDefined() {
        final String url = createUrlToFindProcessesByStatus(
                HANDLING, null, 2
        );
        final ResponseEntity<String> responseEntity = this.restTemplate.getForEntity(url, String.class);

        assertSame(NOT_ACCEPTABLE, responseEntity.getStatusCode());

        final String actual = responseEntity.getBody();
        final String expectedRegex = "\\{\"httpStatus\":\"NOT_ACCEPTABLE\","
                + "\"message\":\"Required request parameter 'pageNumber' for method parameter type Integer "
                + "is not present\","
                + "\"dateTime\":\"\\d{4}-\\d{2}-\\d{2} \\d{2}-\\d{2}-\\d{2}\"}";
        assertNotNull(actual);
        assertTrue(actual.matches(expectedRegex));
    }

    @Test
    public void processesShouldNotBeFoundBecauseOfPageNumberIsLessThanMinimalAllowable() {
        final String url = createUrlToFindProcessesByStatus(
                HANDLING, -1, 2
        );
        final ResponseEntity<String> responseEntity = this.restTemplate.getForEntity(url, String.class);

        assertSame(NOT_ACCEPTABLE, responseEntity.getStatusCode());

        final String actual = responseEntity.getBody();
        final String expectedRegex = "\\{\"httpStatus\":\"NOT_ACCEPTABLE\","
                + "\"message\":\"findByStatus\\.pageNumber: must be greater than or equal to 0\","
                + "\"dateTime\":\"\\d{4}-\\d{2}-\\d{2} \\d{2}-\\d{2}-\\d{2}\"}";
        assertNotNull(actual);
        assertTrue(actual.matches(expectedRegex));
    }

    @Test
    public void processesShouldNotBeFoundBecauseOfPageSizeIsNotDefined() {
        final String url = createUrlToFindProcessesByStatus(
                HANDLING, 0, null
        );
        final ResponseEntity<String> responseEntity = this.restTemplate.getForEntity(url, String.class);

        assertSame(NOT_ACCEPTABLE, responseEntity.getStatusCode());

        final String actual = responseEntity.getBody();
        final String expectedRegex = "\\{\"httpStatus\":\"NOT_ACCEPTABLE\","
                + "\"message\":\"Required request parameter 'pageSize' for method parameter type Integer "
                + "is not present\","
                + "\"dateTime\":\"\\d{4}-\\d{2}-\\d{2} \\d{2}-\\d{2}-\\d{2}\"}";
        assertNotNull(actual);
        assertTrue(actual.matches(expectedRegex));
    }

    @Test
    public void processesShouldNotBeFoundBecauseOfPageSizeIsLessThanMinimalAllowable() {
        final String url = createUrlToFindProcessesByStatus(
                HANDLING, 0, 0
        );
        final ResponseEntity<String> responseEntity = this.restTemplate.getForEntity(url, String.class);

        assertSame(NOT_ACCEPTABLE, responseEntity.getStatusCode());

        final String actual = responseEntity.getBody();
        final String expectedRegex = "\\{\"httpStatus\":\"NOT_ACCEPTABLE\","
                + "\"message\":\"findByStatus\\.pageSize: must be greater than or equal to 1\","
                + "\"dateTime\":\"\\d{4}-\\d{2}-\\d{2} \\d{2}-\\d{2}-\\d{2}\"}";
        assertNotNull(actual);
        assertTrue(actual.matches(expectedRegex));
    }

    @Test
    public void processShouldBeStarted() {
        final String givenBody = "{"
                + "\"areaCoordinate\" : {"
                + "\"leftBottom\" : {"
                + "\"latitude\" : 53.669375,"
                + "\"longitude\" : 27.053689"
                + "},"
                + "\"rightUpper\" : {"
                + "\"latitude\" : 53.896085,"
                + "\"longitude\" : 27.443176"
                + "}"
                + "},"
                + "\"searchStep\": 0.04"
                + "}";

        final HttpHeaders givenHeaders = new HttpHeaders();
        givenHeaders.setContentType(APPLICATION_JSON);

        final HttpEntity<String> givenHttpEntity = new HttpEntity<>(givenBody, givenHeaders);

        final String url = createUrlToStartProcess();

        final SearchingCitiesProcess givenProcess = SearchingCitiesProcess.builder()
                .id(255L)
                .bounds(createPolygon(this.geometryFactory, 1., 2., 3., 4., 5., 6.))
                .searchStep(0.5)
                .totalPoints(100)
                .handledPoints(0)
                .status(HANDLING)
                .build();
        final AreaCoordinate expectedAreaCoordinate = new AreaCoordinate(
                new Coordinate(53.669375, 27.053689),
                new Coordinate(53.896085, 27.443176)
        );
        final double expectedSearchStep = 0.04;
        when(this.mockedStartingProcessService.start(expectedAreaCoordinate, expectedSearchStep))
                .thenReturn(givenProcess);

        final String actual = this.restTemplate.postForObject(url, givenHttpEntity, String.class);
        final String expected = "{\"id\":255,\"bounds\":{\"type\":\"Polygon\","
                + "\"coordinates\":[[[1.0,2.0],[3.0,4.0],[5.0,6.0],[1.0,2.0]]]},"
                + "\"searchStep\":0.5,\"totalPoints\":100,\"handledPoints\":0,\"status\":\"HANDLING\"}";
        assertEquals(expected, actual);
    }

    @Test
    public void processShouldNotBeStartedBecauseOfRequestIsNotValid() {
        final String givenBody = "{"
                + "\"areaCoordinate\" : {"
                + "\"leftBottom\" : {"
                + "\"latitude\" : 53.669375,"
                + "\"longitude\" : 27.053689"
                + "},"
                + "\"rightUpper\" : {"
                + "\"latitude\" : 53.896085,"
                + "\"longitude\" : 27.443176"
                + "}"
                + "}"
                + "}";

        final HttpHeaders givenHeaders = new HttpHeaders();
        givenHeaders.setContentType(APPLICATION_JSON);

        final HttpEntity<String> givenHttpEntity = new HttpEntity<>(givenBody, givenHeaders);

        final String url = createUrlToStartProcess();

        final String actual = this.restTemplate.postForObject(url, givenHttpEntity, String.class);
        final String expectedRegex = "\\{\"httpStatus\":\"NOT_ACCEPTABLE\","
                + "\"message\":\"searchStep : must not be null\","
                + "\"dateTime\":\"\\d{4}-\\d{2}-\\d{2} \\d{2}-\\d{2}-\\d{2}\"}";
        assertNotNull(actual);
        assertTrue(actual.matches(expectedRegex));
    }

    @Test
    public void processShouldBeStartedBecauseOfAreaCoordinateIsNotValid() {
        final String givenBody = "{"
                + "\"areaCoordinate\" : {"
                + "\"leftBottom\" : {"
                + "\"latitude\" : 53.896085,"
                + "\"longitude\" : 27.443176"
                + "},"
                + "\"rightUpper\" : {"
                + "\"latitude\" : 53.896084,"
                + "\"longitude\" : 27.443175"
                + "}"
                + "},"
                + "\"searchStep\": 0.04"
                + "}";

        final HttpHeaders givenHeaders = new HttpHeaders();
        givenHeaders.setContentType(APPLICATION_JSON);

        final HttpEntity<String> givenHttpEntity = new HttpEntity<>(givenBody, givenHeaders);

        final String url = createUrlToStartProcess();

        final String actual = this.restTemplate.postForObject(url, givenHttpEntity, String.class);
        final String expectedRegex = "\\{\"httpStatus\":\"NOT_ACCEPTABLE\","
                + "\"message\":\"Left bottom point's coordinates should be less than right upper point's coordinates.\","
                + "\"dateTime\":\"\\d{4}-\\d{2}-\\d{2} \\d{2}-\\d{2}-\\d{2}\"}";
        assertNotNull(actual);
        assertTrue(actual.matches(expectedRegex));
    }

    private static String createUrlToFindProcessById(final Long id) {
        return CONTROLLER_URL + "/" + id;
    }

    private static String createUrlToFindProcessesByStatus(final Status status,
                                                           final Integer pageNumber,
                                                           final Integer pageSize) {
        final String statusName = status != null ? status.name() : null;
        return createUrlToFindProcessesByStatus(statusName, pageNumber, pageSize);
    }

    private static String createUrlToFindProcessesByStatus(final String statusName,
                                                           final Integer pageNumber,
                                                           final Integer pageSize) {
        final UriComponentsBuilder builder = fromUriString(CONTROLLER_URL);
        if (statusName != null) {
            builder.queryParam(PARAM_NAME_STATUS, statusName);
        }
        if (pageNumber != null) {
            builder.queryParam(PARAM_NAME_PAGE_NUMBER, pageNumber);
        }
        if (pageSize != null) {
            builder.queryParam(PARAM_NAME_PAGE_SIZE, pageSize);
        }
        return builder
                .build()
                .toUriString();
    }

    private static String createUrlToStartProcess() {
        return CONTROLLER_URL;
    }
}
