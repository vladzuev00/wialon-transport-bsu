//package by.bsu.wialontransport.service.report.tableappender;
//
//import by.bsu.wialontransport.crud.dto.Tracker;
//import by.bsu.wialontransport.service.report.model.UserMovementReportBuildingContext;
//import org.junit.Test;
//import org.vandeseer.easytable.structure.Row;
//import org.vandeseer.easytable.structure.cell.AbstractCell;
//
//import java.util.List;
//import java.util.Map;
//
//import static by.bsu.wialontransport.util.CollectionTestUtil.createLinkedHashMap;
//import static by.bsu.wialontransport.util.PDFTestUtil.findCellsContents;
//import static org.junit.Assert.assertEquals;
//
//public final class UserTrackersReportTableAppenderTest {
//    private final AbstractReportTableAppender reportTableAppender = new UserTrackersReportTableAppender(null);
//
//    @Test
//    public void contentRowsShouldBeCreated() {
//        final Map<Tracker, Integer> givenPointCountsByAllTrackers = createLinkedHashMap(
//                createTracker("11112222333344445555", "447736955"), 10,
//                createTracker("11112222333344445556", "447736956"), 7,
//                createTracker("11112222333344445554", "447736957"), 15
//        );
//        final UserMovementReportBuildingContext givenContext = createContext(givenPointCountsByAllTrackers);
//
//        final List<Row> actual = this.reportTableAppender.createContentRows(givenContext);
//        final List<String> actualCellsContents = findCellsContents(actual);
//        final List<String> expectedCellsContents = List.of(
//                "11112222333344445555", "447736955", "10",
//                "11112222333344445556", "447736956", "7",
//                "11112222333344445554", "447736957", "15"
//        );
//        assertEquals(expectedCellsContents, actualCellsContents);
//    }
//
//    @Test
//    public void headerRowCellsShouldBeCreated() {
//        final AbstractCell[] actual = this.reportTableAppender.createHeaderRowCells();
//        final List<String> actualCellsContents = findCellsContents(actual);
//        final List<String> expectedCellsContents = List.of("Imei", "Phone number", "Count of points");
//        assertEquals(expectedCellsContents, actualCellsContents);
//    }
//
//    private static Tracker createTracker(final String imei, final String phoneNumber) {
//        return Tracker.builder()
//                .imei(imei)
//                .phoneNumber(phoneNumber)
//                .build();
//    }
//
//    private static UserMovementReportBuildingContext createContext(final Map<Tracker, Integer> pointCountsByAllTrackers) {
//        return UserMovementReportBuildingContext.builder()
//                .pointCountsByAllTrackers(pointCountsByAllTrackers)
//                .build();
//    }
//}
