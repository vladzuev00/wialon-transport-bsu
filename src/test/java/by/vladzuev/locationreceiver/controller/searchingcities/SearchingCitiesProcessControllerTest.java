//package by.vladzuev.locationreceiver.controller.searchingcities;
//
//import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
//import by.vladzuev.locationreceiver.controller.exception.CustomValidationException;
//import by.vladzuev.locationreceiver.controller.searchingcities.mapper.SearchingCitiesProcessControllerMapper;
//import by.vladzuev.locationreceiver.controller.searchingcities.model.SearchingCitiesProcessResponse;
//import by.vladzuev.locationreceiver.controller.searchingcities.model.StartSearchingCitiesRequest;
//import by.vladzuev.locationreceiver.controller.searchingcities.validator.StartSearchingCitiesRequestValidator;
//import by.vladzuev.locationreceiver.crud.service.SearchingCitiesProcessService;
//import by.vladzuev.locationreceiver.model.AreaCoordinate;
//import by.vladzuev.locationreceiver.model.AreaCoordinateRequest;
//import by.vladzuev.locationreceiver.model.GpsCoordinate;
//import by.vladzuev.locationreceiver.model.CoordinateRequest;
//import by.vladzuev.locationreceiver.service.searchingcities.StartingSearchingCitiesProcessService;
//import by.vladzuev.locationreceiver.crud.entity.SearchingCitiesProcessEntity;
//import by.vladzuev.locationreceiver.util.GeometryTestUtil;
//import org.junit.Test;
//import org.locationtech.jts.geom.Geometry;
//import org.locationtech.jts.geom.GeometryFactory;
//import org.skyscreamer.jsonassert.comparator.CustomComparator;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.util.UriComponentsBuilder;
//import org.wololo.jts2geojson.GeoJSONWriter;
//
//import java.util.List;
//import java.util.Optional;
//
//import static by.vladzuev.locationreceiver.util.GeometryTestUtil.createPolygon;
//import static java.util.Optional.empty;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertSame;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.ArgumentMatchers.same;
//import static org.mockito.Mockito.*;
//import static org.skyscreamer.jsonassert.Customization.customization;
//import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
//import static org.skyscreamer.jsonassert.JSONCompareMode.STRICT;
//import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
//import static org.springframework.http.HttpStatus.*;
//import static org.springframework.http.MediaType.APPLICATION_JSON;
//import static org.springframework.web.util.UriComponentsBuilder.fromUriString;
//
//@SpringBootTest(webEnvironment = RANDOM_PORT)
//public final class SearchingCitiesProcessControllerTest extends AbstractSpringBootTest {
//    private static final String JSON_PROPERTY_NAME_DATE_TIME = "dateTime";
//    private static final CustomComparator COMPARATOR_IGNORING_DATE_TIME = new CustomComparator(
//            STRICT,
//            customization(JSON_PROPERTY_NAME_DATE_TIME, (first, second) -> true)
//    );
//
//    private static final String CONTROLLER_URL = "/searchCities";
//
//    private static final String PARAM_NAME_STATUS = "status";
//    private static final String PARAM_NAME_PAGE_NUMBER = "pageNumber";
//    private static final String PARAM_NAME_PAGE_SIZE = "pageSize";
//
//    @MockBean
//    private StartSearchingCitiesRequestValidator mockedRequestValidator;
//
//    @MockBean
//    private SearchingCitiesProcessService mockedProcessService;
//
//    @MockBean
//    private SearchingCitiesProcessControllerMapper mockedMapper;
//
//    @MockBean
//    private StartingSearchingCitiesProcessService mockedStartingProcessService;
//
//    @Autowired
//    private GeometryFactory geometryFactory;
//
//    @Autowired
//    private GeoJSONWriter geoJSONWriter;
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    @Test
//    public void processShouldBeFoundById()
//            throws Exception {
//        final Long givenId = 255L;
//        final Geometry givenBounds = GeometryTestUtil.createPolygon(
//                geometryFactory,
//                1, 1, 1, 2, 2, 2, 2, 1
//        );
//        final double givenSearchStep = 0.5;
//        final long givenTotalPoints = 100;
//        final long givenHandledPoints = 0;
//        final SearchingCitiesProcessEntity.Status givenStatus = SearchingCitiesProcessEntity.Status.HANDLING;
//
//        final SearchingCitiesProcess givenProcess = SearchingCitiesProcess.builder()
//                .id(givenId)
//                .bounds(givenBounds)
//                .searchStep(givenSearchStep)
//                .totalPoints(givenTotalPoints)
//                .handledPoints(givenHandledPoints)
//                .status(givenStatus)
//                .build();
//        when(mockedProcessService.findById(eq(givenId))).thenReturn(Optional.of(givenProcess));
//
//        final SearchingCitiesProcessResponse givenResponse = SearchingCitiesProcessResponse.builder()
//                .id(givenId)
//                .bounds(geoJSONWriter.write(givenBounds))
//                .searchStep(givenSearchStep)
//                .totalPoints(givenTotalPoints)
//                .handledPoints(givenHandledPoints)
//                .status(givenStatus)
//                .build();
//        when(mockedMapper.mapToResponse(same(givenProcess))).thenReturn(givenResponse);
//
//        final String url = createUrlToFindProcessById(givenId);
//        final ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
//
//        assertSame(OK, responseEntity.getStatusCode());
//
//        final String actual = responseEntity.getBody();
//        final String expected = """
//                {
//                  "id": 255,
//                  "bounds": {
//                    "type": "Polygon",
//                    "coordinates": [
//                      [
//                        [
//                          1,
//                          1
//                        ],
//                        [
//                          1,
//                          2
//                        ],
//                        [
//                          2,
//                          2
//                        ],
//                        [
//                          2,
//                          1
//                        ],
//                        [
//                          1,
//                          1
//                        ]
//                      ]
//                    ]
//                  },
//                  "searchStep": 0.5,
//                  "totalPoints": 100,
//                  "handledPoints": 0,
//                  "status": "HANDLING"
//                }""";
//        assertEquals(expected, actual, true);
//    }
//
//    @Test
//    public void processShouldNotBeFoundById()
//            throws Exception {
//        final Long givenId = 255L;
//
//        when(mockedProcessService.findById(givenId)).thenReturn(empty());
//
//        final String url = createUrlToFindProcessById(givenId);
//        final ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
//
//        assertSame(NOT_FOUND, responseEntity.getStatusCode());
//
//        final String actual = responseEntity.getBody();
//        final String expected = """
//                {
//                  "httpStatus": "NOT_FOUND",
//                  "message": "Process with id '255' doesn't exist.",
//                  "dateTime": "2024-01-05 21-34-41"
//                }""";
//        assertNotNull(actual);
//        assertEquals(expected, actual, COMPARATOR_IGNORING_DATE_TIME);
//    }
//
//    @Test
//    public void processesShouldBeFoundByStatuses()
//            throws Exception {
//        final SearchingCitiesProcessEntity.Status givenStatus = SearchingCitiesProcessEntity.Status.HANDLING;
//        final int givenPageNumber = 0;
//        final int givenPageSize = 2;
//
//        final Long firstGivenProcessId = 255L;
//        final Geometry firstGivenProcessGeometry = GeometryTestUtil.createPolygon(
//                geometryFactory,
//                1, 1, 1, 2, 2, 2, 2, 1
//        );
//        final double firstGivenProcessSearchStep = 0.5;
//        final long firstGivenProcessTotalPoints = 100;
//        final long firstGivenProcessHandledPoints = 0;
//        final SearchingCitiesProcess firstGivenProcess = SearchingCitiesProcess.builder()
//                .id(firstGivenProcessId)
//                .bounds(firstGivenProcessGeometry)
//                .searchStep(firstGivenProcessSearchStep)
//                .totalPoints(firstGivenProcessTotalPoints)
//                .handledPoints(firstGivenProcessHandledPoints)
//                .status(givenStatus)
//                .build();
//
//        final Long secondGivenProcessId = 256L;
//        final Geometry secondGivenProcessGeometry = GeometryTestUtil.createPolygon(
//                geometryFactory,
//                1, 1, 1, 2, 2, 2, 2, 1
//        );
//        final double secondGivenProcessSearchStep = 0.5;
//        final long secondGivenProcessTotalPoints = 100;
//        final long secondGivenProcessHandledPoints = 0;
//        final SearchingCitiesProcess secondGivenProcess = SearchingCitiesProcess.builder()
//                .id(secondGivenProcessId)
//                .bounds(secondGivenProcessGeometry)
//                .searchStep(secondGivenProcessSearchStep)
//                .totalPoints(secondGivenProcessTotalPoints)
//                .handledPoints(secondGivenProcessHandledPoints)
//                .status(givenStatus)
//                .build();
//
//        final Page<SearchingCitiesProcess> givenProcesses = new PageImpl<>(
//                List.of(firstGivenProcess, secondGivenProcess)
//        );
//        final PageRequest expectedPageRequest = PageRequest.of(givenPageNumber, givenPageSize);
//        when(mockedProcessService.findByStatusOrderedById(same(givenStatus), eq(expectedPageRequest)))
//                .thenReturn(givenProcesses);
//
//        final SearchingCitiesProcessResponse firstGivenResponse = SearchingCitiesProcessResponse.builder()
//                .id(firstGivenProcessId)
//                .bounds(geoJSONWriter.write(firstGivenProcessGeometry))
//                .searchStep(firstGivenProcessSearchStep)
//                .totalPoints(firstGivenProcessTotalPoints)
//                .handledPoints(firstGivenProcessHandledPoints)
//                .status(givenStatus)
//                .build();
//        when(mockedMapper.mapToResponse(same(firstGivenProcess))).thenReturn(firstGivenResponse);
//
//        final SearchingCitiesProcessResponse secondGivenResponse = SearchingCitiesProcessResponse.builder()
//                .id(secondGivenProcessId)
//                .bounds(geoJSONWriter.write(secondGivenProcessGeometry))
//                .searchStep(secondGivenProcessSearchStep)
//                .totalPoints(secondGivenProcessTotalPoints)
//                .handledPoints(secondGivenProcessHandledPoints)
//                .status(givenStatus)
//                .build();
//        when(mockedMapper.mapToResponse(same(secondGivenProcess))).thenReturn(secondGivenResponse);
//
//        final String url = createUrlToFindProcessesByStatus(givenStatus, givenPageNumber, givenPageSize);
//        final ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
//
//        assertSame(OK, responseEntity.getStatusCode());
//
//        final String actual = responseEntity.getBody();
//        final String expected = """
//                {
//                  "content": [
//                    {
//                      "id": 255,
//                      "bounds": {
//                        "type": "Polygon",
//                        "coordinates": [
//                          [
//                            [
//                              1,
//                              1
//                            ],
//                            [
//                              1,
//                              2
//                            ],
//                            [
//                              2,
//                              2
//                            ],
//                            [
//                              2,
//                              1
//                            ],
//                            [
//                              1,
//                              1
//                            ]
//                          ]
//                        ]
//                      },
//                      "searchStep": 0.5,
//                      "totalPoints": 100,
//                      "handledPoints": 0,
//                      "status": "HANDLING"
//                    },
//                    {
//                      "id": 256,
//                      "bounds": {
//                        "type": "Polygon",
//                        "coordinates": [
//                          [
//                            [
//                              1,
//                              1
//                            ],
//                            [
//                              1,
//                              2
//                            ],
//                            [
//                              2,
//                              2
//                            ],
//                            [
//                              2,
//                              1
//                            ],
//                            [
//                              1,
//                              1
//                            ]
//                          ]
//                        ]
//                      },
//                      "searchStep": 0.5,
//                      "totalPoints": 100,
//                      "handledPoints": 0,
//                      "status": "HANDLING"
//                    }
//                  ],
//                  "pageable": "INSTANCE",
//                  "last": true,
//                  "totalElements": 2,
//                  "totalPages": 1,
//                  "size": 2,
//                  "number": 0,
//                  "sort": {
//                    "empty": true,
//                    "sorted": false,
//                    "unsorted": true
//                  },
//                  "first": true,
//                  "numberOfElements": 2,
//                  "empty": false
//                }""";
//        assertEquals(expected, actual, true);
//    }
//
//    @Test
//    public void processesShouldNotBeFoundBecauseOfStatusIsNotDefined()
//            throws Exception {
//        final Integer givenPageNumber = 0;
//        final Integer givenPageSize = 2;
//
//        final String url = createUrlToFindProcessesByStatus((SearchingCitiesProcessEntity.Status) null, givenPageNumber, givenPageSize);
//        final ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
//
//        assertSame(NOT_ACCEPTABLE, responseEntity.getStatusCode());
//
//        final String actual = responseEntity.getBody();
//        final String expected = """
//                {
//                  "httpStatus": "NOT_ACCEPTABLE",
//                  "message": "Required request parameter 'status' for method parameter type Status is not present",
//                  "dateTime": "2024-01-05 22-30-16"
//                }""";
//        assertNotNull(actual);
//        assertEquals(expected, actual, COMPARATOR_IGNORING_DATE_TIME);
//    }
//
//    @Test
//    public void processesShouldNotBeFoundBecauseOfNotValidStatus()
//            throws Exception {
//        final String givenStatusName = "some-status";
//        final Integer givenPageNumber = 0;
//        final Integer givenPageSize = 2;
//
//        final String url = createUrlToFindProcessesByStatus(givenStatusName, givenPageNumber, givenPageSize);
//        final ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
//
//        assertSame(NOT_ACCEPTABLE, responseEntity.getStatusCode());
//
//        final String actual = responseEntity.getBody();
//        final String expected = """
//                {
//                  "httpStatus": "NOT_ACCEPTABLE",
//                  "message": "'some-status' should be replaced by one of: HANDLING, SUCCESS, ERROR.",
//                  "dateTime": "2024-01-05 22-56-05"
//                }""";
//        assertNotNull(actual);
//        assertEquals(expected, actual, COMPARATOR_IGNORING_DATE_TIME);
//    }
//
//    @Test
//    public void processesShouldNotBeFoundBecauseOfPageNumberIsNotDefined()
//            throws Exception {
//        final Integer givenPageSize = 2;
//
//        final String url = createUrlToFindProcessesByStatus(
//                SearchingCitiesProcessEntity.Status.HANDLING,
//                null,
//                givenPageSize
//        );
//        final ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
//
//        assertSame(NOT_ACCEPTABLE, responseEntity.getStatusCode());
//
//        final String actual = responseEntity.getBody();
//        final String expected = """
//                {
//                  "httpStatus": "NOT_ACCEPTABLE",
//                  "message": "Required request parameter 'pageNumber' for method parameter type Integer is not present",
//                  "dateTime": "2024-01-05 22-59-32"
//                }""";
//        assertNotNull(actual);
//        assertEquals(expected, actual, COMPARATOR_IGNORING_DATE_TIME);
//    }
//
//    @Test
//    public void processesShouldNotBeFoundBecauseOfPageNumberIsLessThanMinimalAllowable()
//            throws Exception {
//        final Integer givenPageNumber = -1;
//        final Integer givenPageSize = 2;
//
//        final String url = createUrlToFindProcessesByStatus(
//                SearchingCitiesProcessEntity.Status.HANDLING,
//                givenPageNumber,
//                givenPageSize
//        );
//        final ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
//
//        assertSame(NOT_ACCEPTABLE, responseEntity.getStatusCode());
//
//        final String actual = responseEntity.getBody();
//        final String expected = """
//                {
//                  "httpStatus": "NOT_ACCEPTABLE",
//                  "message": "findByStatus.pageNumber: must be greater than or equal to 0",
//                  "dateTime": "2024-01-05 23-03-07"
//                }""";
//        assertNotNull(actual);
//        assertEquals(expected, actual, COMPARATOR_IGNORING_DATE_TIME);
//    }
//
//    @Test
//    public void processesShouldNotBeFoundBecauseOfPageSizeIsNotDefined()
//            throws Exception {
//        final Integer givenPageNumber = 0;
//
//        final String url = createUrlToFindProcessesByStatus(
//                SearchingCitiesProcessEntity.Status.HANDLING,
//                givenPageNumber,
//                null
//        );
//        final ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
//
//        assertSame(NOT_ACCEPTABLE, responseEntity.getStatusCode());
//
//        final String actual = responseEntity.getBody();
//        final String expected = """
//                {
//                  "httpStatus": "NOT_ACCEPTABLE",
//                  "message": "Required request parameter 'pageSize' for method parameter type Integer is not present",
//                  "dateTime": "2024-01-05 23-06-54"
//                }""";
//        assertNotNull(actual);
//        assertEquals(expected, actual, COMPARATOR_IGNORING_DATE_TIME);
//    }
//
//    @Test
//    public void processesShouldNotBeFoundBecauseOfPageSizeIsLessThanMinimalAllowable()
//            throws Exception {
//        final Integer givenPageNumber = 0;
//        final Integer givenPageSize = 0;
//
//        final String url = createUrlToFindProcessesByStatus(
//                SearchingCitiesProcessEntity.Status.HANDLING,
//                givenPageNumber,
//                givenPageSize
//        );
//        final ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
//
//        assertSame(NOT_ACCEPTABLE, responseEntity.getStatusCode());
//
//        final String actual = responseEntity.getBody();
//        final String expected = """
//                {
//                  "httpStatus": "NOT_ACCEPTABLE",
//                  "message": "findByStatus.pageSize: must be greater than or equal to 1",
//                  "dateTime": "2024-01-05 23-10-27"
//                }""";
//        assertNotNull(actual);
//        assertEquals(expected, actual, COMPARATOR_IGNORING_DATE_TIME);
//    }
//
//    @Test
//    public void processShouldBeStarted()
//            throws Exception {
//        final AreaCoordinateRequest givenAreaCoordinateRequest = new AreaCoordinateRequest(
//                new CoordinateRequest(53.669375, 27.053689),
//                new CoordinateRequest(53.896085, 27.443176)
//        );
//        final double givenSearchStep = 0.04;
//        final StartSearchingCitiesRequest givenRequest = new StartSearchingCitiesRequest(
//                givenAreaCoordinateRequest,
//                givenSearchStep
//        );
//        final HttpEntity<StartSearchingCitiesRequest> givenHttpEntity = createHttpEntityToStartProcess(givenRequest);
//
//        final String url = createUrlToStartProcess();
//
//        final AreaCoordinate givenAreaCoordinate = new AreaCoordinate(
//                new GpsCoordinate(53.669375, 27.053689),
//                new GpsCoordinate(53.896085, 27.443176)
//        );
//        when(mockedMapper.mapToAreaCoordinate(eq(givenAreaCoordinateRequest))).thenReturn(givenAreaCoordinate);
//
//        final Long givenProcessId = 255L;
//        final Geometry givenProcessGeometry = GeometryTestUtil.createPolygon(
//                geometryFactory,
//                1., 2., 3., 4., 5., 6.
//        );
//        final long givenTotalPoints = 100;
//        final long givenHandledPoints = 0;
//        final SearchingCitiesProcessEntity.Status givenStatus = SearchingCitiesProcessEntity.Status.HANDLING;
//        final SearchingCitiesProcess givenProcess = SearchingCitiesProcess.builder()
//                .id(givenProcessId)
//                .bounds(givenProcessGeometry)
//                .searchStep(givenSearchStep)
//                .totalPoints(givenTotalPoints)
//                .handledPoints(givenHandledPoints)
//                .status(givenStatus)
//                .build();
//        when(mockedStartingProcessService.start(same(givenAreaCoordinate), eq(givenSearchStep)))
//                .thenReturn(givenProcess);
//
//        final SearchingCitiesProcessResponse givenResponse = SearchingCitiesProcessResponse.builder()
//                .id(givenProcessId)
//                .bounds(geoJSONWriter.write(givenProcessGeometry))
//                .searchStep(givenSearchStep)
//                .totalPoints(givenTotalPoints)
//                .handledPoints(givenHandledPoints)
//                .status(givenStatus)
//                .build();
//        when(mockedMapper.mapToResponse(same(givenProcess))).thenReturn(givenResponse);
//
//        final String actual = restTemplate.postForObject(url, givenHttpEntity, String.class);
//        final String expected = """
//                {
//                  "id": 255,
//                  "bounds": {
//                    "type": "Polygon",
//                    "coordinates": [
//                      [
//                        [
//                          1,
//                          2
//                        ],
//                        [
//                          3,
//                          4
//                        ],
//                        [
//                          5,
//                          6
//                        ],
//                        [
//                          1,
//                          2
//                        ]
//                      ]
//                    ]
//                  },
//                  "searchStep": 0.04,
//                  "totalPoints": 100,
//                  "handledPoints": 0,
//                  "status": "HANDLING"
//                }""";
//        assertEquals(expected, actual, true);
//
//        verify(mockedRequestValidator, times(1)).validate(eq(givenRequest));
//    }
//
//    @Test
//    public void processShouldNotBeStartedBecauseOfRequestIsNotValid()
//            throws Exception {
//        final AreaCoordinateRequest givenAreaCoordinateRequest = new AreaCoordinateRequest(
//                new CoordinateRequest(53.669375, 27.053689),
//                new CoordinateRequest(53.896085, 27.443176)
//        );
//        final StartSearchingCitiesRequest givenRequest = StartSearchingCitiesRequest.builder()
//                .areaCoordinate(givenAreaCoordinateRequest)
//                .build();
//        final HttpEntity<StartSearchingCitiesRequest> givenHttpEntity = createHttpEntityToStartProcess(givenRequest);
//
//        final String url = createUrlToStartProcess();
//
//        final String actual = restTemplate.postForObject(url, givenHttpEntity, String.class);
//        final String expected = """
//                {
//                  "httpStatus": "NOT_ACCEPTABLE",
//                  "message": "searchStep : must not be null",
//                  "dateTime": "2024-01-06 08-53-13"
//                }""";
//        assertNotNull(actual);
//        assertEquals(expected, actual, COMPARATOR_IGNORING_DATE_TIME);
//    }
//
//    @Test
//    public void processShouldBeStartedBecauseOfAreaCoordinateIsNotValid()
//            throws Exception {
//        final AreaCoordinateRequest givenAreaCoordinateRequest = new AreaCoordinateRequest(
//                new CoordinateRequest(53.669375, 27.053689),
//                new CoordinateRequest(53.896085, 27.443176)
//        );
//        final double givenSearchStep = 0.04;
//        final StartSearchingCitiesRequest givenRequest = new StartSearchingCitiesRequest(
//                givenAreaCoordinateRequest,
//                givenSearchStep
//        );
//        final HttpEntity<StartSearchingCitiesRequest> givenHttpEntity = createHttpEntityToStartProcess(givenRequest);
//
//        final String url = createUrlToStartProcess();
//
//        final Exception givenException = new CustomValidationException("Request isn't valid");
//        doThrow(givenException).when(mockedRequestValidator).validate(eq(givenRequest));
//
//        final String actual = restTemplate.postForObject(url, givenHttpEntity, String.class);
//        final String expected = """
//                {
//                  "httpStatus": "NOT_ACCEPTABLE",
//                  "message": "Request isn't valid",
//                  "dateTime": "2024-01-06 08-58-48"
//                }""";
//        assertNotNull(actual);
//        assertEquals(expected, actual, COMPARATOR_IGNORING_DATE_TIME);
//    }
//
//    private static String createUrlToFindProcessById(final Long id) {
//        return CONTROLLER_URL + "/" + id;
//    }
//
//    private static String createUrlToFindProcessesByStatus(final SearchingCitiesProcessEntity.Status status,
//                                                           final Integer pageNumber,
//                                                           final Integer pageSize) {
//        final String statusName = status != null ? status.name() : null;
//        return createUrlToFindProcessesByStatus(statusName, pageNumber, pageSize);
//    }
//
//    private static String createUrlToFindProcessesByStatus(final String statusName,
//                                                           final Integer pageNumber,
//                                                           final Integer pageSize) {
//        final UriComponentsBuilder builder = fromUriString(CONTROLLER_URL);
//        if (statusName != null) {
//            builder.queryParam(PARAM_NAME_STATUS, statusName);
//        }
//        if (pageNumber != null) {
//            builder.queryParam(PARAM_NAME_PAGE_NUMBER, pageNumber);
//        }
//        if (pageSize != null) {
//            builder.queryParam(PARAM_NAME_PAGE_SIZE, pageSize);
//        }
//        return builder.build().toUriString();
//    }
//
//    private static String createUrlToStartProcess() {
//        return CONTROLLER_URL;
//    }
//
//    private HttpEntity<StartSearchingCitiesRequest> createHttpEntityToStartProcess(
//            final StartSearchingCitiesRequest request
//    ) {
//        final HttpHeaders givenHeaders = new HttpHeaders();
//        givenHeaders.setContentType(APPLICATION_JSON);
//        return new HttpEntity<>(request, givenHeaders);
//    }
//}
