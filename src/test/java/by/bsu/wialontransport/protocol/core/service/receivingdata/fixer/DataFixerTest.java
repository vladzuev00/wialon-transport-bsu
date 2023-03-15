package by.bsu.wialontransport.protocol.core.service.receivingdata.fixer;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Data.Latitude;
import by.bsu.wialontransport.crud.dto.Data.Longitude;

import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.dto.Tracker;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

import static by.bsu.wialontransport.crud.entity.DataEntity.Latitude.Type.NORTH;
import static by.bsu.wialontransport.crud.entity.DataEntity.Latitude.Type.SOUTH;
import static by.bsu.wialontransport.crud.entity.DataEntity.Longitude.Type.EAST;
import static by.bsu.wialontransport.crud.entity.DataEntity.Longitude.Type.WESTERN;
import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.DOUBLE;

import static org.junit.Assert.assertTrue;

public final class DataFixerTest {
    private final DataFixer dataFixer;

    public DataFixerTest() {
        this.dataFixer = new DataFixer();
    }

    @Test
    public void dataShouldBeFixed() {
        final Data givenFixed = Data.builder()
                .id(255L)
                .date(LocalDate.of(2023, 11, 3))
                .time(LocalTime.of(19, 28, 0))
                .latitude(Latitude.builder()
                        .degrees(1)
                        .minutes(2)
                        .minuteShare(3)
                        .type(NORTH)
                        .build())
                .longitude(Longitude.builder()
                        .degrees(4)
                        .minutes(5)
                        .minuteShare(6)
                        .type(EAST)
                        .build())
                .speed(7)
                .course(8)
                .altitude(9)
                .amountOfSatellites(10)
                .reductionPrecision(11)
                .inputs(12)
                .outputs(13)
                .analogInputs(new double[]{0.1, 0.2, 0.3})
                .driverKeyCode("driver key code")
                .parametersByNames(Map.of(
                        "122", createDoubleParameter("par122", "14"),
                        "124", createDoubleParameter("124", "16"),
                        "125", createDoubleParameter("125", "17")
                ))
                .tracker(createTracker())
                .build();

        final Data givenPrevious = Data.builder()
                .id(256L)
                .date(LocalDate.of(2023, 11, 3))
                .time(LocalTime.of(19, 27, 59))
                .latitude(Latitude.builder()
                        .degrees(3)
                        .minutes(4)
                        .minuteShare(5)
                        .type(SOUTH)
                        .build())
                .longitude(Longitude.builder()
                        .degrees(4)
                        .minutes(6)
                        .minuteShare(7)
                        .type(WESTERN)
                        .build())
                .speed(9)
                .course(10)
                .altitude(11)
                .amountOfSatellites(15)
                .reductionPrecision(12)
                .inputs(13)
                .outputs(14)
                .analogInputs(new double[]{0.1, 0.2, 0.3, 0.4})
                .driverKeyCode("driver key code 2")
                .parametersByNames(Map.of(
                        "122", createDoubleParameter("122", "11"),
                        "par123", createDoubleParameter("123", "14"),
                        "124", createDoubleParameter("par124", "17"),
                        "125", createDoubleParameter("125", "14")
                ))
                .tracker(createTracker())
                .build();

        final Data actual = this.dataFixer.fix(givenFixed, givenPrevious);

        //alias as key of map for 123 parameter is chosen randomly so there are two expected objects
        final Data firstExpected = Data.builder()
                .id(255L)
                .date(LocalDate.of(2023, 11, 3))
                .time(LocalTime.of(19, 28, 0))
                .latitude(Latitude.builder()
                        .degrees(3)
                        .minutes(4)
                        .minuteShare(5)
                        .type(SOUTH)
                        .build())
                .longitude(Longitude.builder()
                        .degrees(4)
                        .minutes(6)
                        .minuteShare(7)
                        .type(WESTERN)
                        .build())
                .speed(7)
                .course(8)
                .altitude(9)
                .amountOfSatellites(15)
                .reductionPrecision(11)
                .inputs(12)
                .outputs(13)
                .analogInputs(new double[]{0.1, 0.2, 0.3})
                .driverKeyCode("driver key code")
                .parametersByNames(Map.of(
                        "122", createDoubleParameter("122", "11"),
                        "par123", createDoubleParameter("123", "14"),
                        "124", createDoubleParameter("par124", "17"),
                        "125", createDoubleParameter("125", "17")
                ))
                .tracker(createTracker())
                .build();
        final Data secondExpected = Data.builder()
                .id(255L)
                .date(LocalDate.of(2023, 11, 3))
                .time(LocalTime.of(19, 28, 0))
                .latitude(Latitude.builder()
                        .degrees(3)
                        .minutes(4)
                        .minuteShare(5)
                        .type(SOUTH)
                        .build())
                .longitude(Longitude.builder()
                        .degrees(4)
                        .minutes(6)
                        .minuteShare(7)
                        .type(WESTERN)
                        .build())
                .speed(7)
                .course(8)
                .altitude(9)
                .amountOfSatellites(15)
                .reductionPrecision(11)
                .inputs(12)
                .outputs(13)
                .analogInputs(new double[]{0.1, 0.2, 0.3})
                .driverKeyCode("driver key code")
                .parametersByNames(Map.of(
                        "122", createDoubleParameter("122", "11"),
                        "123", createDoubleParameter("123", "14"),
                        "124", createDoubleParameter("par124", "17"),
                        "125", createDoubleParameter("125", "17")
                ))
                .tracker(createTracker())
                .build();

        assertTrue(actual.equals(firstExpected) || actual.equals(secondExpected));
    }

    private static Parameter createDoubleParameter(final String name, final String value) {
        return Parameter.builder()
                .name(name)
                .type(DOUBLE)
                .value(value)
                .build();
    }

    private static Tracker createTracker() {
        return Tracker.builder()
                .id(255L)
                .build();
    }
}
