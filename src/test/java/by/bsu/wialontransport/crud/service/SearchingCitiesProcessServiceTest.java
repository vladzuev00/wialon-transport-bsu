package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import by.bsu.wialontransport.crud.dto.SearchingCitiesProcess;
import by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity.Status;
import org.junit.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity.Status.ERROR;
import static by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity.Status.HANDLING;
import static by.bsu.wialontransport.util.GeometryTestUtil.createPolygon;
import static java.lang.Long.MAX_VALUE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class SearchingCitiesProcessServiceTest extends AbstractSpringBootTest {

    @Autowired
    private SearchingCitiesProcessService service;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    @Sql("classpath:sql/searching-cities-process/insert-searching-cities-processes.sql")
    public void statusOfProcessShouldBeUpdated() {
        final Long givenId = 255L;
        final Status givenNewStatus = ERROR;
        final SearchingCitiesProcess givenProcess = createSearchingCitiesProcess(givenId);

        final int actualCountUpdatedRows = service.updateStatus(givenProcess, givenNewStatus);
        final int expectedCountUpdatedRows = 1;
        assertEquals(expectedCountUpdatedRows, actualCountUpdatedRows);

        final SearchingCitiesProcess actual = service.findById(givenId).orElseThrow();
        final SearchingCitiesProcess expected = SearchingCitiesProcess.builder()
                .id(givenId)
                .bounds(createPolygon(geometryFactory, 1, 1, 1, 4, 4, 4, 4, 1))
                .searchStep(0.5)
                .totalPoints(1000)
                .handledPoints(100)
                .status(givenNewStatus)
                .build();
        assertEquals(expected, actual);
    }

    @Test
    @Sql("classpath:sql/searching-cities-process/insert-searching-cities-processes.sql")
    public void statusOfProcessShouldNotBeUpdatedBecauseOfNotExistingProcessId() {
        final Long givenId = MAX_VALUE;
        final SearchingCitiesProcess givenProcess = createSearchingCitiesProcess(givenId);

        final int actualCountUpdatedRows = service.updateStatus(givenProcess, ERROR);
        final int expectedCountUpdatedRows = 0;
        assertEquals(expectedCountUpdatedRows, actualCountUpdatedRows);
    }

    @Test
    @Sql("classpath:sql/searching-cities-process/insert-searching-cities-processes.sql")
    public void handledPointsShouldBeIncreased() {
        final Long givenId = 255L;
        final long givenDelta = 100;
        final SearchingCitiesProcess givenProcess = createSearchingCitiesProcess(givenId);

        final int actualCountUpdatedRows = service.increaseHandledPoints(givenProcess, givenDelta);
        final int expectedCountUpdatedRows = 1;
        assertEquals(expectedCountUpdatedRows, actualCountUpdatedRows);

        final SearchingCitiesProcess actual = service.findById(givenId).orElseThrow();
        final SearchingCitiesProcess expected = SearchingCitiesProcess.builder()
                .id(255L)
                .bounds(createPolygon(geometryFactory, 1, 1, 1, 4, 4, 4, 4, 1))
                .searchStep(0.5)
                .totalPoints(1000)
                .handledPoints(200)
                .status(HANDLING)
                .build();
        assertEquals(expected, actual);
    }

    @Test
    @Sql("classpath:sql/searching-cities-process/insert-searching-cities-processes.sql")
    public void handledPointsShouldNotBeIncreasedBecauseOfNotExistingProcessId() {
        final Long givenId = 258L;
        final long givenDelta = 100;
        final SearchingCitiesProcess givenProcess = createSearchingCitiesProcess(givenId);

        final int actualCountUpdatedRows = service.increaseHandledPoints(givenProcess, givenDelta);
        final int expectedCountUpdatedRows = 0;
        assertEquals(expectedCountUpdatedRows, actualCountUpdatedRows);
    }

    @Test
    @Sql("classpath:sql/searching-cities-process/insert-searching-cities-processes.sql")
    public void processesOrderedByIdShouldBeFoundByStatus() {
        final Status givenStatus = HANDLING;
        final PageRequest givenPageRequest = PageRequest.of(0, 4);

        final Page<SearchingCitiesProcess> actual = service.findByStatusOrderedById(givenStatus, givenPageRequest);
        final List<SearchingCitiesProcess> actualAsList = actual.toList();
        final List<SearchingCitiesProcess> expectedAsList = List.of(
                SearchingCitiesProcess.builder()
                        .id(255L)
                        .bounds(createPolygon(geometryFactory, 1, 1, 1, 4, 4, 4, 4, 1))
                        .searchStep(0.5)
                        .totalPoints(1000)
                        .handledPoints(100)
                        .status(givenStatus)
                        .build(),
                SearchingCitiesProcess.builder()
                        .id(256L)
                        .bounds(createPolygon(geometryFactory, 1, 1, 1, 4, 4, 4, 4, 1))
                        .searchStep(0.5)
                        .totalPoints(1000)
                        .handledPoints(100)
                        .status(givenStatus)
                        .build()
        );
        assertEquals(expectedAsList, actualAsList);
    }

    @Test
    @Sql("classpath:sql/searching-cities-process/insert-searching-cities-processes.sql")
    public void processesOrderedByIdShouldNotBeFoundByStatus() {
        final PageRequest givenPageRequest = PageRequest.of(0, 4);

        final Page<SearchingCitiesProcess> actual = service.findByStatusOrderedById(ERROR, givenPageRequest);
        assertTrue(actual.isEmpty());
    }

    private SearchingCitiesProcess createSearchingCitiesProcess(final Long id) {
        return SearchingCitiesProcess.builder()
                .id(id)
                .build();
    }
}
