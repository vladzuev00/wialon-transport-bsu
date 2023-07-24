//package by.bsu.wialontransport.service.report.tableappender;
//
//import by.bsu.wialontransport.crud.dto.Address;
//import by.bsu.wialontransport.crud.dto.Data;
//import by.bsu.wialontransport.crud.dto.Data.Latitude;
//import by.bsu.wialontransport.crud.dto.Data.Longitude;
//import by.bsu.wialontransport.crud.dto.Tracker;
//import by.bsu.wialontransport.crud.entity.DataEntity;
//import by.bsu.wialontransport.service.report.model.UserMovementReportBuildingContext;
//import org.junit.Test;
//import org.vandeseer.easytable.structure.Row;
//import org.vandeseer.easytable.structure.cell.AbstractCell;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.List;
//import java.util.Map;
//
//import static by.bsu.wialontransport.crud.entity.DataEntity.Latitude.Type.NORTH;
//import static by.bsu.wialontransport.crud.entity.DataEntity.Longitude.Type.WESTERN;
//import static by.bsu.wialontransport.util.CollectionTestUtil.createLinkedHashMap;
//import static by.bsu.wialontransport.util.PDFTestUtil.findCellsContents;
//import static org.junit.Assert.assertEquals;
//
//public final class UserMovementReportTableAppenderTest {
//    private final AbstractReportTableAppender reportTableAppender = new UserMovementReportTableAppender(null);
//
//    @Test
//    public void contentRowsShouldBeCreated() {
//        final Map<Tracker, List<Data>> givenDataGroupedBySortedByImeiTrackers = createLinkedHashMap(
//                createTracker("11112222333344445555"),
//                List.of(
//                        createData(
//                                LocalDateTime.of(2023, 7, 19, 21, 16, 0),
//                                createLatitude(1, 2, 3, NORTH),
//                                createLongitude(4, 5, 6, WESTERN),
//                                "first-city",
//                                "first-country"
//                        ),
//                        createData(
//                                LocalDateTime.of(2023, 7, 19, 21, 16, 1),
//                                createLatitude(1, 2, 5, NORTH),
//                                createLongitude(4, 5, 6, WESTERN),
//                                "first-city",
//                                "first-country"
//                        )
//                ),
//
//                createTracker("11112222333344445556"),
//                List.of(
//                        createData(
//                                LocalDateTime.of(2023, 7, 19, 21, 16, 5),
//                                createLatitude(1, 2, 3, NORTH),
//                                createLongitude(4, 5, 6, WESTERN),
//                                "second-city",
//                                "second-country"
//                        )
//                )
//        );
//        final UserMovementReportBuildingContext givenContext = createContext(givenDataGroupedBySortedByImeiTrackers);
//
//        final List<Row> actual = this.reportTableAppender.createContentRows(givenContext);
//        final List<String> actualCellsContents = findCellsContents(actual);
//        final List<String> expectedCellsContents = List.of(
//                "11112222333344445555", "19-07-2023 21:16:00", "1.03417", "-4.085", "first-city", "first-country",
//                "11112222333344445555", "19-07-2023 21:16:01", "1.03472", "-4.085", "first-city", "first-country",
//                "11112222333344445556", "19-07-2023 21:16:05", "1.03417", "-4.085", "second-city", "second-country"
//        );
//        assertEquals(expectedCellsContents, actualCellsContents);
//    }
//
//    @Test
//    public void headerRowCellsShouldBeCreated() {
//        final AbstractCell[] actual = this.reportTableAppender.createHeaderRowCells();
//        final List<String> actualCellsContents = findCellsContents(actual);
//        final List<String> expectedCellsContents = List.of(
//                "Tracker's imei", "Datetime", "Latitude", "Longitude", "City", "Country"
//        );
//        assertEquals(expectedCellsContents, actualCellsContents);
//    }
//
//    private static Tracker createTracker(final String imei) {
//        return Tracker.builder()
//                .imei(imei)
//                .build();
//    }
//
//    private static Data createData(final LocalDateTime dateTime,
//                                   final Latitude latitude,
//                                   final Longitude longitude,
//                                   final String addressCityName,
//                                   final String addressCountryName) {
//        final LocalDate date = dateTime.toLocalDate();
//        final LocalTime time = dateTime.toLocalTime();
//        final Address address = createAddress(addressCityName, addressCountryName);
//        return Data.builder()
//                .date(date)
//                .time(time)
//                .latitude(latitude)
//                .longitude(longitude)
//                .address(address)
//                .build();
//    }
//
//    private static Address createAddress(final String cityName, final String countryName) {
//        return Address.builder()
//                .cityName(cityName)
//                .countryName(countryName)
//                .build();
//    }
//
//    private static Latitude createLatitude(final int degrees,
//                                           final int minutes,
//                                           final int minuteShare,
//                                           final DataEntity.Latitude.Type type) {
//        return Latitude.builder()
//                .degrees(degrees)
//                .minutes(minutes)
//                .minuteShare(minuteShare)
//                .type(type)
//                .build();
//    }
//
//    private static Longitude createLongitude(final int degrees,
//                                             final int minutes,
//                                             final int minuteShare,
//                                             final DataEntity.Longitude.Type type) {
//        return Longitude.builder()
//                .degrees(degrees)
//                .minutes(minutes)
//                .minuteShare(minuteShare)
//                .type(type)
//                .build();
//    }
//
//    private static UserMovementReportBuildingContext createContext(final Map<Tracker, List<Data>> dataGroupedBySortedByImeiTrackers) {
//        return UserMovementReportBuildingContext.builder()
//                .dataBySortedByImeiTrackers(dataGroupedBySortedByImeiTrackers)
//                .build();
//    }
//}
