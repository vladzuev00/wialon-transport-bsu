package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.Data;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static by.bsu.wialontransport.crud.entity.DataEntity.Latitude.Type.NORTH;
import static by.bsu.wialontransport.crud.entity.DataEntity.Longitude.Type.EAST;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class DataServiceTest extends AbstractContextTest {

    @Autowired
    private DataService service;

    @Test
    @Sql(statements = "INSERT INTO tracker_last_data"
            + "(id, date, time, "
            + "latitude_degrees, latitude_minutes, latitude_minute_share, latitude_type, "
            + "longitude_degrees, longitude_minutes, longitude_minute_share, longitude_type, "
            + "speed, course, height, amount_of_satellites, tracker_id) "
            + "VALUES(255, '2019-10-24', '14:39:53', 1, 2, 3, 'N', 5, 6, 7, 'E', 8, 9, 10, 11, 255)")
    public void trackerLastDataShouldBeFoundByTrackerId() {
        final Data actual = this.service.findTrackerLastData(255L).orElseThrow();
        final Data expected = Data.dataBuilder()
                .id(255L)
                .date(LocalDate.of(2019, 10, 24))
                .time(LocalTime.of(14, 39, 53))
                .latitude(Data.Latitude.builder()
                        .degrees(1)
                        .minutes(2)
                        .minuteShare(3)
                        .type(NORTH)
                        .build())
                .longitude(Data.Longitude.builder()
                        .degrees(5)
                        .minutes(6)
                        .minuteShare(7)
                        .type(EAST)
                        .build())
                .speed(8)
                .course(9)
                .height(10)
                .amountOfSatellites(11)
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void trackerLastDataShouldNotBeFoundByTrackerId() {
        final Optional<Data> optionalFoundData = this.service.findTrackerLastData(255L);
        assertTrue(optionalFoundData.isEmpty());
    }
}
