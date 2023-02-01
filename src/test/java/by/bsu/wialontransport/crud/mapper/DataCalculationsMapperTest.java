package by.bsu.wialontransport.crud.mapper;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.DataCalculations;
import by.bsu.wialontransport.crud.entity.DataCalculationsEntity;
import by.bsu.wialontransport.crud.entity.DataEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;

import static by.bsu.wialontransport.crud.entity.DataEntity.Latitude.Type.NORTH;
import static by.bsu.wialontransport.crud.entity.DataEntity.Longitude.Type.EAST;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public final class DataCalculationsMapperTest extends AbstractContextTest {

    @Autowired
    private DataCalculationsMapper mapper;

    @Test
    public void dtoShouldBeMappedToEntity() {
        final DataCalculations givenDto = DataCalculations.builder()
                .id(256L)
                .gpsOdometer(10.5)
                .ignitionOn(true)
                .engineOnDurationSeconds(11)
                .data(Data.builder()
                        .id(257L)
                        .date(LocalDate.of(20212, 11, 15))
                        .time(LocalTime.of(15, 44, 22))
                        .latitude(Data.Latitude.builder()
                                .degrees(30)
                                .minutes(31)
                                .minuteShare(32)
                                .type(NORTH)
                                .build())
                        .longitude(Data.Longitude.builder()
                                .degrees(33)
                                .minutes(34)
                                .minuteShare(35)
                                .type(EAST)
                                .build())
                        .speed(36)
                        .course(37)
                        .altitude(38)
                        .amountOfSatellites(39)
                        .build())
                .build();

        final DataCalculationsEntity actual = this.mapper.mapToEntity(givenDto);
        final DataCalculationsEntity expected = DataCalculationsEntity.builder()
                .id(256L)
                .gpsOdometer(10.5)
                .ignitionOn(true)
                .engineOnDurationSeconds(11)
                .data(super.entityManager.getReference(DataEntity.class, 257L))
                .build();

        assertNotNull(actual);
        checkEquals(expected, actual);
    }

    //TODO: add test to map entity to dto

    private static void checkEquals(final DataCalculationsEntity expected, final DataCalculationsEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getGpsOdometer(), actual.getGpsOdometer(), 0.);
        assertEquals(expected.isIgnitionOn(), actual.isIgnitionOn());
        assertEquals(expected.getEngineOnDurationSeconds(), actual.getEngineOnDurationSeconds());
        assertEquals(expected.getData(), actual.getData());
    }
}
