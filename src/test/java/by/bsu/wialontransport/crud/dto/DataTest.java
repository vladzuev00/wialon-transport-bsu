package by.bsu.wialontransport.crud.dto;

import by.bsu.wialontransport.crud.dto.Data.Latitude;
import by.bsu.wialontransport.crud.dto.Data.Longitude;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static by.bsu.wialontransport.crud.dto.Data.createWithAddress;
import static by.bsu.wialontransport.crud.dto.Data.createWithTracker;
import static by.bsu.wialontransport.crud.entity.DataEntity.Latitude.Type.NORTH;
import static by.bsu.wialontransport.crud.entity.DataEntity.Longitude.Type.EAST;
import static org.junit.Assert.assertEquals;

public final class DataTest {

    @Test
    public void dataShouldBeCopiedWithNewTracker() {
        final Data givenData = Data.builder()
                .id(255L)
                .date(LocalDate.of(2022, 11, 15))
                .time(LocalTime.of(15, 44, 22))
                .latitude(Latitude.builder()
                        .degrees(30)
                        .minutes(31)
                        .minuteShare(32)
                        .type(NORTH)
                        .build())
                .longitude(Longitude.builder()
                        .degrees(33)
                        .minutes(34)
                        .minuteShare(35)
                        .type(EAST)
                        .build())
                .speed(36)
                .course(37)
                .altitude(38)
                .amountOfSatellites(39)
                .address(createAddress(258L))
                .build();
        final Tracker givenTracker = createTracker(259L);

        final Data actual = createWithTracker(givenData, givenTracker);
        final Data expected = Data.builder()
                .id(255L)
                .date(LocalDate.of(2022, 11, 15))
                .time(LocalTime.of(15, 44, 22))
                .latitude(Latitude.builder()
                        .degrees(30)
                        .minutes(31)
                        .minuteShare(32)
                        .type(NORTH)
                        .build())
                .longitude(Longitude.builder()
                        .degrees(33)
                        .minutes(34)
                        .minuteShare(35)
                        .type(EAST)
                        .build())
                .speed(36)
                .course(37)
                .altitude(38)
                .amountOfSatellites(39)
                .address(createAddress(258L))
                .tracker(givenTracker)
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void dataShouldBeCopiedWithNewAddress() {
        final Data givenData = Data.builder()
                .id(255L)
                .date(LocalDate.of(2022, 11, 15))
                .time(LocalTime.of(15, 44, 22))
                .latitude(Latitude.builder()
                        .degrees(30)
                        .minutes(31)
                        .minuteShare(32)
                        .type(NORTH)
                        .build())
                .longitude(Longitude.builder()
                        .degrees(33)
                        .minutes(34)
                        .minuteShare(35)
                        .type(EAST)
                        .build())
                .speed(36)
                .course(37)
                .altitude(38)
                .amountOfSatellites(39)
                .tracker(createTracker(259L))
                .build();
        final Address givenAddress = createAddress(258L);

        final Data actual = createWithAddress(givenData, givenAddress);
        final Data expected = Data.builder()
                .id(255L)
                .date(LocalDate.of(2022, 11, 15))
                .time(LocalTime.of(15, 44, 22))
                .latitude(Latitude.builder()
                        .degrees(30)
                        .minutes(31)
                        .minuteShare(32)
                        .type(NORTH)
                        .build())
                .longitude(Longitude.builder()
                        .degrees(33)
                        .minutes(34)
                        .minuteShare(35)
                        .type(EAST)
                        .build())
                .speed(36)
                .course(37)
                .altitude(38)
                .amountOfSatellites(39)
                .tracker(createTracker(259L))
                .address(givenAddress)
                .build();
        assertEquals(expected, actual);
    }

    private static Address createAddress(Long id) {
        return Address.builder()
                .id(id)
                .build();
    }

    private static Tracker createTracker(Long id) {
        return Tracker.builder()
                .id(id)
                .build();
    }
}
