package by.bsu.wialontransport.service.report.tableappender;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.service.report.model.UserMovementReportBuildingContext;
import org.junit.Test;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.cell.AbstractCell;
import org.vandeseer.easytable.structure.cell.TextCell;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static by.bsu.wialontransport.util.CellFactoryUtil.createTextCell;
import static by.bsu.wialontransport.util.CollectionUtil.createLinkedHashMap;
import static org.junit.Assert.assertEquals;

public final class UserTrackersReportTableAppenderTest {
    private final AbstractReportTableAppender reportTableAppender = new UserTrackersReportTableAppender(null);

    @Test
    public void contentRowsShouldBeCreated() {
        final Map<Tracker, Integer> givenPointCountsByAllTrackers = createLinkedHashMap(
                createTracker("11112222333344445555", "447736955"), 10,
                createTracker("11112222333344445556", "447736956"), 7,
                createTracker("11112222333344445554", "447736957"), 15
        );
        final UserMovementReportBuildingContext givenContext = createContext(givenPointCountsByAllTrackers);

        final List<Row> actual = this.reportTableAppender.createContentRows(givenContext);
        final List<String> actualRowsContents = findColumnContents(actual);
        final List<String> expectedRowsContents = List.of(
                "11112222333344445555", "447736955", "10",
                "11112222333344445556", "447736956", "7",
                "11112222333344445554", "447736957", "15"
        );
        assertEquals(expectedRowsContents, actualRowsContents);
    }

    @Test
    public void headerRowCellsShouldBeCreated() {
        final AbstractCell[] actual = this.reportTableAppender.createHeaderRowCells();
        throw new RuntimeException();
    }

    private static Tracker createTracker(final String imei, final String phoneNumber) {
        return Tracker.builder()
                .imei(imei)
                .phoneNumber(phoneNumber)
                .build();
    }

    private static UserMovementReportBuildingContext createContext(final Map<Tracker, Integer> pointCountsByAllTrackers) {
        return UserMovementReportBuildingContext.builder()
                .pointCountsByAllTrackers(pointCountsByAllTrackers)
                .build();
    }

    private static List<String> findColumnContents(final List<Row> rows) {
        return rows.stream()
                .flatMap(UserTrackersReportTableAppenderTest::findColumnContentsAsStream)
                .toList();
    }

    private static Stream<String> findColumnContentsAsStream(final Row row) {
        return row.getCells()
                .stream()
                .map(cell -> (TextCell) cell)
                .map(TextCell::getText);
    }

    private static Row createContentRow(final String trackerImei,
                                        final String trackerPhoneNumber,
                                        final Integer pointCounts) {
        return Row.builder()
                .add(createTextCell(trackerImei))
                .add(createTextCell(trackerPhoneNumber))
                .add(createTextCell(pointCounts))
                .build();
    }
}
