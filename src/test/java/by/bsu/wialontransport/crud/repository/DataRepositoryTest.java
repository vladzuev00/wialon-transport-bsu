package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.entity.AddressEntity;
import by.bsu.wialontransport.crud.entity.DataEntity;
import by.bsu.wialontransport.crud.entity.DataEntity.Coordinate;
import by.bsu.wialontransport.crud.entity.ParameterEntity;
import by.bsu.wialontransport.crud.entity.TrackerEntity;
import by.bsu.wialontransport.util.entity.EntityUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.INTEGER;
import static by.bsu.wialontransport.util.StreamUtil.isEmpty;
import static by.bsu.wialontransport.util.entity.DataEntityUtil.*;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.*;

public final class DataRepositoryTest extends AbstractContextTest {

    @Autowired
    private DataRepository repository;

    @Test
    @Sql("classpath:sql/data/insert-data.sql")
    public void dataShouldBeFoundById() {
        startQueryCount();
        final DataEntity actual = repository.findById(256L).orElseThrow();
        checkQueryCount(1);

        assertFalse(areParametersLoaded(actual));
        assertFalse(isTrackerLoaded(actual));
        assertFalse(isAddressLoaded(actual));

        final DataEntity expected = DataEntity.builder()
                .id(256L)
                .dateTime(LocalDateTime.of(2019, 10, 24, 14, 39, 52))
                .coordinate(new Coordinate(53.233, 27.3434))
                .speed(8)
                .course(9)
                .altitude(10)
                .amountOfSatellites(11)
                .reductionPrecision(12.4)
                .inputs(13)
                .outputs(14)
                .analogInputs(new double[]{0.2, 0.3, 0.4})
                .driverKeyCode("driver key code")
                .parameters(List.of(entityManager.getReference(ParameterEntity.class, 257L)))
                .tracker(entityManager.getReference(TrackerEntity.class, 255L))
                .address(entityManager.getReference(AddressEntity.class, 258L))
                .build();
        checkEquals(expected, actual);
    }

    @Test
    @Sql("classpath:sql/data/insert-data.sql")
    public void dataShouldBeInserted() {
        final DataEntity givenData = DataEntity.builder()
                .dateTime(LocalDateTime.of(2019, 10, 24, 14, 39, 53))
                .coordinate(new Coordinate(53.233, 27.3434))
                .speed(8)
                .course(9)
                .altitude(10)
                .amountOfSatellites(11)
                .reductionPrecision(12.4)
                .inputs(13)
                .outputs(14)
                .analogInputs(new double[]{0.2, 0.3, 0.4})
                .driverKeyCode("driver key code")
                .tracker(entityManager.getReference(TrackerEntity.class, 255L))
                .address(entityManager.getReference(AddressEntity.class, 258L))
                .build();
        final List<ParameterEntity> givenParameters = List.of(ParameterEntity.builder()
                .name("name")
                .type(INTEGER)
                .value("44")
                .data(givenData)
                .build()
        );
        givenData.setParameters(givenParameters);

        startQueryCount();
        repository.save(givenData);
        checkQueryCount(4);
    }

    @Test
    @Sql("classpath:sql/data/insert-data.sql")
    public void trackerLastDataShouldBeFoundByTrackerId() {
        startQueryCount();
        final DataEntity actual = repository.findTrackerLastDataByTrackerId(255L).orElseThrow();
        checkQueryCount(1);

        assertTrue(areParametersLoaded(actual));
        assertFalse(isTrackerLoaded(actual));
        assertFalse(isAddressLoaded(actual));

        final DataEntity expected = DataEntity.builder()
                .id(257L)
                .dateTime(LocalDateTime.of(2019, 10, 26, 14, 39, 53))
                .coordinate(new Coordinate(53.233, 27.3434))
                .speed(8)
                .course(9)
                .altitude(10)
                .amountOfSatellites(11)
                .reductionPrecision(12.4)
                .inputs(13)
                .outputs(14)
                .analogInputs(new double[]{0.2, 0.3, 0.4})
                .driverKeyCode("driver key code")
                .parameters(emptyList())
                .tracker(entityManager.getReference(TrackerEntity.class, 255L))
                .address(entityManager.getReference(AddressEntity.class, 258L))
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void trackerLastDataShouldNotBeFoundByTrackerId() {
        startQueryCount();
        final Optional<DataEntity> actual = repository.findTrackerLastDataByTrackerId(256L);
        checkQueryCount(1);

        assertTrue(actual.isEmpty());
    }

    @Test
    @Sql("classpath:sql/data/insert-data.sql")
    public void dataWithTrackerAndAddressShouldBeFoundByUserId() {
        final Long givenUserId = 255L;
        final LocalDateTime givenStartDateTime = LocalDateTime.of(2019, 10, 23, 0, 0, 0);
        final LocalDateTime givenEndDateTime = LocalDateTime.of(2019, 10, 25, 0, 0, 0);

        startQueryCount();
        try (final Stream<DataEntity> actual = repository.findDataWithTrackerAndAddressByUserId(givenUserId, givenStartDateTime, givenEndDateTime)) {
            checkQueryCount(1);

            final Set<DataEntity> actualAsSet = actual.collect(toSet());

            assertTrue(areParametersNotLoaded(actualAsSet));
            assertTrue(areTrackersLoaded(actualAsSet));
            assertTrue(areAddressesLoaded(actualAsSet));

            final Set<Long> actualIds = EntityUtil.mapToIdsSet(actualAsSet);
            final Set<Long> expectedIds = Set.of(254L, 255L, 256L);
            assertEquals(expectedIds, actualIds);
        }
    }

    @Test
    @Sql("classpath:sql/data/insert-data.sql")
    public void dataWithTrackerAndAddressOfUserShouldNotBeFound() {
        final Long givenUserId = 255L;
        final LocalDateTime givenStartDateTime = LocalDateTime.of(2015, 10, 23, 0, 0, 0);
        final LocalDateTime givenEndDateTime = LocalDateTime.of(2015, 10, 25, 0, 0, 0);

        try (final Stream<DataEntity> actual = repository.findDataWithTrackerAndAddressByUserId(givenUserId, givenStartDateTime, givenEndDateTime)) {
            assertTrue(isEmpty(actual));
        }
    }
}
