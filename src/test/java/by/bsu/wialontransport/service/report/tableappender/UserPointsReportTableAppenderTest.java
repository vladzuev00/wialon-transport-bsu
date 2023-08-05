package by.bsu.wialontransport.service.report.tableappender;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Data.Latitude;
import by.bsu.wialontransport.crud.dto.Data.Longitude;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.entity.DataEntity;
import by.bsu.wialontransport.service.report.model.TrackerMovement;

import org.junit.Test;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.cell.AbstractCell;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Stream;

import static by.bsu.wialontransport.crud.entity.DataEntity.Latitude.Type.NORTH;
import static by.bsu.wialontransport.crud.entity.DataEntity.Longitude.Type.EAST;
import static by.bsu.wialontransport.util.PDFTestUtil.findCellContents;
import static org.junit.Assert.assertEquals;

public final class UserPointsReportTableAppenderTest {
    private final UserPointsReportTableAppender tableAppender = new UserPointsReportTableAppender(null);

    @Test
    public void streamRowShouldBeCreated() {
        final String givenTrackerImei = "11112222333344445555";
        final Tracker givenTracker = createTracker(givenTrackerImei);

        final List<Data> givenData = List.of(
                createData(
                        LocalDateTime.of(2023, 10, 10, 10, 10, 10),
                        createLatitude(44, 55, 66, NORTH),
                        createLongitude(77, 88, 99, EAST),
                        "first-city",
                        "first-country"
                ),
                createData(
                        LocalDateTime.of(2023, 10, 10, 9, 9, 9),
                        createLatitude(11, 22, 33, NORTH),
                        createLongitude(44, 55, 66, EAST),
                        "second-city",
                        "second-country"
                )
        );

        final TrackerMovement givenTrackerMovement = createTrackerMovement(givenTracker, givenData);

        final Stream<Row> actual = this.tableAppender.createContentRowStream(givenTrackerMovement);
        final List<String> actualCellContents = findCellContents(actual);
        final List<String> expectedCellContents = List.of(
                "11112222333344445555", "10-10-2023 09:09:09", "11.37583", "44.935", "second-city", "second-country",
                "11112222333344445555", "10-10-2023 10:10:10", "44.935", "78.49417", "first-city", "first-country"
        );
        assertEquals(expectedCellContents, actualCellContents);
    }

    @Test
    public void headerRowCellsShouldBeCreated() {
        final AbstractCell[] actual = this.tableAppender.createHeaderRowCells();
        final List<String> actualCellContents = findCellContents(actual);
        final List<String> expectedCellContents = List.of(
                "Tracker's imei", "Datetime", "Latitude", "Longitude", "City", "Country"
        );
        assertEquals(expectedCellContents, actualCellContents);
    }

    private static Tracker createTracker(final String imei) {
        return Tracker.builder()
                .imei(imei)
                .build();
    }

    private static Data createData(final LocalDateTime dateTime,
                                   final Latitude latitude,
                                   final Longitude longitude,
                                   final String addressCityName,
                                   final String addressCountryName) {
        final LocalDate date = dateTime.toLocalDate();
        final LocalTime time = dateTime.toLocalTime();
        final Address address = createAddress(addressCityName, addressCountryName);
        return Data.builder()
                .date(date)
                .time(time)
                .latitude(latitude)
                .longitude(longitude)
                .address(address)
                .build();
    }

    private static Address createAddress(final String cityName, final String countryName) {
        return Address.builder()
                .cityName(cityName)
                .countryName(countryName)
                .build();
    }

    private static Latitude createLatitude(final int degrees,
                                           final int minutes,
                                           final int minuteShare,
                                           final DataEntity.Latitude.Type type) {
        return Latitude.builder()
                .degrees(degrees)
                .minutes(minutes)
                .minuteShare(minuteShare)
                .type(type)
                .build();
    }

    private static Longitude createLongitude(final int degrees,
                                             final int minutes,
                                             final int minuteShare,
                                             final DataEntity.Longitude.Type type) {
        return Longitude.builder()
                .degrees(degrees)
                .minutes(minutes)
                .minuteShare(minuteShare)
                .type(type)
                .build();
    }

    private static TrackerMovement createTrackerMovement(final Tracker tracker, final List<Data> data) {
        return TrackerMovement.builder()
                .tracker(tracker)
                .data(data)
                .build();
    }
}
