package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Data.Latitude;
import by.bsu.wialontransport.crud.dto.Data.Longitude;
import by.bsu.wialontransport.crud.dto.Parameter;

import by.bsu.wialontransport.crud.dto.Tracker;
import org.junit.Test;
import org.locationtech.jts.geom.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.Optional;

import static by.bsu.wialontransport.crud.entity.DataEntity.Latitude.Type.NORTH;
import static by.bsu.wialontransport.crud.entity.DataEntity.Longitude.Type.EAST;
import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.INTEGER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class DataServiceTest extends AbstractContextTest {

    @Autowired
    private DataService dataService;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name) "
            + "VALUES(258, ST_GeomFromText('POLYGON((2.5 0, 2.5 2.5, 5 2.5, 5 0, 2.5 0))', 4326), "
            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country')")
    @Sql(statements = "INSERT INTO data"
            + "(id, date, time, "
            + "latitude_degrees, latitude_minutes, latitude_minute_share, latitude_type, "
            + "longitude_degrees, longitude_minutes, longitude_minute_share, longitude_type, "
            + "speed, course, altitude, amount_of_satellites, reduction_precision, inputs, outputs, analog_inputs, "
            + "driver_key_code, tracker_id, address_id) "
            + "VALUES(255, '2019-10-24', '14:39:52', 1, 2, 3, 'N', 5, 6, 7, 'E', 8, 9, 10, 11, 12.4, 13, 14, "
            + "ARRAY[0.2, 0.3, 0.4], 'driver key code', 255, 258)")
    @Sql(statements = "INSERT INTO data"
            + "(id, date, time, "
            + "latitude_degrees, latitude_minutes, latitude_minute_share, latitude_type, "
            + "longitude_degrees, longitude_minutes, longitude_minute_share, longitude_type, "
            + "speed, course, altitude, amount_of_satellites, reduction_precision, inputs, outputs, analog_inputs, "
            + "driver_key_code, tracker_id, address_id) "
            + "VALUES(256, '2019-10-24', '14:39:53', 1, 2, 3, 'N', 5, 6, 7, 'E', 8, 9, 10, 11, 12.4, 13, 14, "
            + "ARRAY[0.2, 0.3, 0.4], 'driver key code', 255, 258)")
    @Sql(statements = "INSERT INTO parameters(id, name, type, value, data_id) "
            + "VALUES(257, 'name', 'INTEGER', '44', 256)")
    public void trackerLastDataShouldBeFoundByTrackerId() {
        final Data actual = this.dataService.findTrackerLastDataByTrackerId(255L).orElseThrow();
        final Data expected = Data.builder()
                .id(256L)
                .date(LocalDate.of(2019, 10, 24))
                .time(LocalTime.of(14, 39, 53))
                .latitude(Latitude.builder()
                        .degrees(1)
                        .minutes(2)
                        .minuteShare(3)
                        .type(NORTH)
                        .build())
                .longitude(Longitude.builder()
                        .degrees(5)
                        .minutes(6)
                        .minuteShare(7)
                        .type(EAST)
                        .build())
                .speed(8)
                .course(9)
                .altitude(10)
                .amountOfSatellites(11)
                .reductionPrecision(12.4)
                .inputs(13)
                .outputs(14)
                .analogInputs(new double[]{0.2, 0.3, 0.4})
                .driverKeyCode("driver key code")
                .parametersByNames(Map.of("name", Parameter.builder()
                        .id(257L)
                        .name("name")
                        .type(INTEGER)
                        .value("44")
                        .build()))
                .tracker(Tracker.builder()
                        .id(255L)
                        .imei("11112222333344445555")
                        .password("password")
                        .phoneNumber("447336934")
                        .build())
                .address(Address.builder()
                        .id(258L)
                        .boundingBox(this.createPolygon(2.5,0, 2.5, 2.5, 5, 2.5, 5, 0))
                        .center(this.createPoint(53.050286, 24.873635))
                        .cityName("city")
                        .countryName("country")
                        .build())
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void trackerLastDataShouldNotBeFoundByTrackerId() {
        final Optional<Data> optionalActual = this.dataService.findTrackerLastDataByTrackerId(255L);
        assertTrue(optionalActual.isEmpty());
    }

    private Point createPoint(final double longitude, final double latitude) {
        final CoordinateXY coordinate = new CoordinateXY(longitude, latitude);
        return this.geometryFactory.createPoint(coordinate);
    }

    private Geometry createPolygon(final double firstLongitude, final double firstLatitude,
                                   final double secondLongitude, final double secondLatitude,
                                   final double thirdLongitude, final double thirdLatitude,
                                   final double fourthLongitude, final double fourthLatitude) {
        return this.geometryFactory.createPolygon(new Coordinate[]{
                        new CoordinateXY(firstLongitude, firstLatitude),
                        new CoordinateXY(secondLongitude, secondLatitude),
                        new CoordinateXY(thirdLongitude, thirdLatitude),
                        new CoordinateXY(fourthLongitude, fourthLatitude),
                        new CoordinateXY(firstLongitude, firstLatitude)
                }
        );
    }
}
