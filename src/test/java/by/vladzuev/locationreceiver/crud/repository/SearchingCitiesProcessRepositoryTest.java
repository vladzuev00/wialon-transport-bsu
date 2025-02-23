//package by.vladzuev.locationreceiver.crud.repository;
//
//import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
//import by.vladzuev.locationreceiver.crud.entity.SearchingCitiesProcessEntity;
//import by.vladzuev.locationreceiver.util.GeometryTestUtil;
//import by.vladzuev.locationreceiver.util.PageUtil;
//import by.vladzuev.locationreceiver.util.entity.SearchingCitiesProcessEntityUtil;
//import org.junit.Test;
//import org.locationtech.jts.geom.GeometryFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.test.context.jdbc.Sql;
//
//import java.util.List;
//import java.util.Optional;
//
//import static by.vladzuev.locationreceiver.util.GeometryTestUtil.createPolygon;
//import static java.lang.Long.MAX_VALUE;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//import static org.springframework.data.domain.Pageable.ofSize;
//
//public final class SearchingCitiesProcessRepositoryTest extends AbstractSpringBootTest {
//
//    @Autowired
//    private SearchingCitiesProcessRepository repository;
//
//    @Autowired
//    private GeometryFactory geometryFactory;
//
//    @Test
//    @Sql("classpath:sql/searching-cities-process/insert-searching-cities-processes.sql")
//    public void processShouldBeFoundById() {
//        final Long givenId = 255L;
//
//        startQueryCount();
//        final Optional<SearchingCitiesProcessEntity> optionalActual = repository.findById(givenId);
//        checkQueryCount(1);
//
//        assertTrue(optionalActual.isPresent());
//        final SearchingCitiesProcessEntity actual = optionalActual.get();
//        final SearchingCitiesProcessEntity expected = SearchingCitiesProcessEntity.builder()
//                .id(givenId)
//                .bounds(GeometryTestUtil.createPolygon(geometryFactory, 1, 1, 1, 4, 4, 4, 4, 1))
//                .searchStep(0.5)
//                .totalPoints(1000)
//                .handledPoints(100)
//                .status(SearchingCitiesProcessEntity.Status.HANDLING)
//                .build();
//        SearchingCitiesProcessEntityUtil.checkEquals(expected, actual);
//    }
//
//    @Test
//    public void processShouldBeSaved() {
//        final SearchingCitiesProcessEntity givenProcess = SearchingCitiesProcessEntity.builder()
//                .bounds(GeometryTestUtil.createPolygon(geometryFactory, 1, 1, 1, 4, 4, 4, 4, 1))
//                .searchStep(0.5)
//                .totalPoints(1000)
//                .handledPoints(100)
//                .status(SearchingCitiesProcessEntity.Status.HANDLING)
//                .build();
//
//        startQueryCount();
//        repository.save(givenProcess);
//        checkQueryCount(1);
//    }
//
//    @Test
//    @Sql("classpath:sql/searching-cities-process/insert-searching-cities-processes.sql")
//    public void statusOfProcessShouldBeUpdated() {
//        final Long givenId = 255L;
//        final SearchingCitiesProcessEntity.Status givenNewStatus = SearchingCitiesProcessEntity.Status.SUCCESS;
//
//        startQueryCount();
//        final int actualCountUpdatedRows = repository.updateStatus(givenId, givenNewStatus);
//        checkQueryCount(1);
//
//        final int expectedCountUpdatedRows = 1;
//        assertEquals(expectedCountUpdatedRows, actualCountUpdatedRows);
//
//        final SearchingCitiesProcessEntity actual = repository.findById(givenId).orElseThrow();
//        final SearchingCitiesProcessEntity expected = SearchingCitiesProcessEntity.builder()
//                .id(givenId)
//                .bounds(GeometryTestUtil.createPolygon(geometryFactory, 1, 1, 1, 4, 4, 4, 4, 1))
//                .searchStep(0.5)
//                .totalPoints(1000)
//                .handledPoints(100)
//                .status(givenNewStatus)
//                .build();
//        SearchingCitiesProcessEntityUtil.checkEquals(expected, actual);
//    }
//
//    @Test
//    @Sql("classpath:sql/searching-cities-process/insert-searching-cities-processes.sql")
//    public void statusOfProcessShouldBeUpdatedBecauseOfNotExistingProcessId() {
//        final Long givenId = MAX_VALUE;
//
//        startQueryCount();
//        final int actualCountUpdatedRows = repository.updateStatus(givenId, SearchingCitiesProcessEntity.Status.SUCCESS);
//        checkQueryCount(1);
//
//        final int expectedCountUpdatedRows = 0;
//        assertEquals(expectedCountUpdatedRows, actualCountUpdatedRows);
//    }
//
//    @Test
//    @Sql("classpath:sql/searching-cities-process/insert-searching-cities-processes.sql")
//    public void handledPointsShouldBeIncreased() {
//        final Long givenId = 255L;
//        final long givenDelta = 100;
//
//        startQueryCount();
//        final int actualCountUpdatedRows = repository.increaseHandledPoints(givenId, givenDelta);
//        checkQueryCount(1);
//
//        final int expectedCountUpdatedRows = 1;
//        assertEquals(expectedCountUpdatedRows, actualCountUpdatedRows);
//
//        final SearchingCitiesProcessEntity actual = repository.findById(givenId).orElseThrow();
//        final SearchingCitiesProcessEntity expected = SearchingCitiesProcessEntity.builder()
//                .id(givenId)
//                .bounds(GeometryTestUtil.createPolygon(geometryFactory, 1, 1, 1, 4, 4, 4, 4, 1))
//                .searchStep(0.5)
//                .totalPoints(1000)
//                .handledPoints(200)
//                .status(SearchingCitiesProcessEntity.Status.HANDLING)
//                .build();
//        SearchingCitiesProcessEntityUtil.checkEquals(expected, actual);
//    }
//
//    @Test
//    @Sql("classpath:sql/searching-cities-process/insert-searching-cities-processes.sql")
//    public void handledPointsShouldBeIncreasedBecauseOfNotExistingProcessId() {
//        final Long givenId = MAX_VALUE;
//        final long givenDelta = 100;
//
//        startQueryCount();
//        final int actualCountUpdatedRows = repository.increaseHandledPoints(MAX_VALUE, givenDelta);
//        checkQueryCount(1);
//
//        final int expectedCountUpdatedRows = 0;
//        assertEquals(expectedCountUpdatedRows, actualCountUpdatedRows);
//    }
//
//    @Test
//    @Sql("classpath:sql/searching-cities-process/insert-searching-cities-processes.sql")
//    public void processesOrderedByIdShouldBeFoundByStatus() {
//        final Pageable givenPageable = ofSize(4);
//
//        startQueryCount();
//        final Page<SearchingCitiesProcessEntity> actual = repository.findByStatusOrderedById(SearchingCitiesProcessEntity.Status.HANDLING, givenPageable);
//        checkQueryCount(1);
//
//        final List<Long> actualIds = PageUtil.mapToIds(actual);
//        final List<Long> expectedIds = List.of(255L, 256L);
//        assertEquals(expectedIds, actualIds);
//    }
//
//    @Test
//    @Sql("classpath:sql/searching-cities-process/insert-searching-cities-processes.sql")
//    public void processesOrderedByIdShouldNotBeFoundByStatus() {
//        final Pageable givenPageable = ofSize(4);
//
//        startQueryCount();
//        final Page<SearchingCitiesProcessEntity> actual = repository.findByStatusOrderedById(SearchingCitiesProcessEntity.Status.ERROR, givenPageable);
//        checkQueryCount(1);
//
//        assertTrue(actual.isEmpty());
//    }
//}
