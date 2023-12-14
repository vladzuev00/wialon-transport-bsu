package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.entity.AddressEntity;
import by.bsu.wialontransport.crud.entity.DataEntity;
import by.bsu.wialontransport.crud.entity.DataEntity.Coordinate;
import by.bsu.wialontransport.crud.entity.ParameterEntity;
import by.bsu.wialontransport.crud.entity.TrackerEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.INTEGER;
import static by.bsu.wialontransport.util.entity.DataEntityUtil.*;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class DataRepositoryTest extends AbstractContextTest {

    @Autowired
    private DataRepository repository;

    @Test
    @Sql("classpath:sql/data/insert-data.sql")
    public void dataShouldBeFoundById() {
        super.startQueryCount();
        final DataEntity actual = this.repository.findById(256L).orElseThrow();
        super.checkQueryCount(1);

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
                .parameters(List.of(super.entityManager.getReference(ParameterEntity.class, 257L)))
                .tracker(super.entityManager.getReference(TrackerEntity.class, 255L))
                .address(super.entityManager.getReference(AddressEntity.class, 258L))
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
                .tracker(super.entityManager.getReference(TrackerEntity.class, 255L))
                .address(super.entityManager.getReference(AddressEntity.class, 258L))
                .build();
        final List<ParameterEntity> givenParameters = List.of(ParameterEntity.builder()
                .name("name")
                .type(INTEGER)
                .value("44")
                .data(givenData)
                .build()
        );
        givenData.setParameters(givenParameters);

        super.startQueryCount();
        this.repository.save(givenData);
        super.checkQueryCount(4);
    }

    @Test
    @Sql("classpath:sql/data/insert-data.sql")
    public void trackerLastDataShouldBeFoundByTrackerId() {
        super.startQueryCount();
        final DataEntity actual = this.repository.findTrackerLastDataByTrackerId(255L).orElseThrow();
        super.checkQueryCount(1);

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
                .tracker(super.entityManager.getReference(TrackerEntity.class, 255L))
                .address(super.entityManager.getReference(AddressEntity.class, 258L))
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void trackerLastDataShouldNotBeFoundByTrackerId() {
        super.startQueryCount();
        final Optional<DataEntity> actual = this.repository.findTrackerLastDataByTrackerId(256L);
        super.checkQueryCount(1);

        assertTrue(actual.isEmpty());
    }

//    @Test
//    @Sql("classpath:sql/data/insert-data.sql")
//    public void dataWithTrackerAndAddressShouldBeFoundByUserId() {
//        final Long givenUserId = 255L;
//        final LocalDate givenStartDate = LocalDate.of(2019, 10, 23);
//        final LocalDate givenEndDate = LocalDate.of(2019, 10, 25);
//
//        final List<DataEntity> actual = this.repository.findDataWithTrackerAndAddressByUserId(
//                givenUserId,
//                givenStartDate,
//                givenEndDate
//        );
//        final List<Long> actualIds = findEntityIds(actual);
//        final List<Long> expectedIds = List.of(255L);
//        assertEquals(expectedIds, actualIds);
//
//        final boolean trackersLoaded = areEntityPropertiesLoaded(actual, DataEntity::getTracker);
//        assertTrue(trackersLoaded);
//
//        final boolean addressesLoaded = areEntityPropertiesLoaded(actual, DataEntity::getAddress);
//        assertTrue(addressesLoaded);
//    }

//    @Test
//    public void dataWithTrackerAndAddressOfUserShouldNotBeFound() {
//        final Long givenUserId = 255L;
//        final LocalDate givenStartDate = LocalDate.of(2019, 10, 23);
//        final LocalDate givenEndDate = LocalDate.of(2019, 10, 25);
//
//        final List<DataEntity> actual = this.repository.findDataWithTrackerAndAddressOfUser(
//                givenUserId, givenStartDate, givenEndDate
//        );
//        assertTrue(actual.isEmpty());
//    }
//
//    private static void checkEquals(final DataEntity expected, final DataEntity actual) {
//        assertEquals(expected.getId(), actual.getId());
//        assertEquals(expected.getDate(), actual.getDate());
//        assertEquals(expected.getTime(), actual.getTime());
//        assertEquals(expected.getLatitude(), actual.getLatitude());
//        assertEquals(expected.getLongitude(), actual.getLongitude());
//        assertEquals(expected.getSpeed(), actual.getSpeed());
//        assertEquals(expected.getCourse(), actual.getCourse());
//        assertEquals(expected.getAltitude(), actual.getAltitude());
//        assertEquals(expected.getAmountOfSatellites(), actual.getAmountOfSatellites());
//        assertEquals(expected.getReductionPrecision(), actual.getReductionPrecision(), 0.);
//        assertEquals(expected.getInputs(), actual.getInputs());
//        assertEquals(expected.getOutputs(), actual.getOutputs());
//        assertArrayEquals(expected.getAnalogInputs(), actual.getAnalogInputs(), 0.);
//        assertEquals(expected.getParameters(), actual.getParameters());
//        assertEquals(expected.getTracker(), actual.getTracker());
//        assertEquals(expected.getAddress(), actual.getAddress());
//    }
}
