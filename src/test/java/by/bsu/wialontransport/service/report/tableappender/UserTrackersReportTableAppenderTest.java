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

import static by.bsu.wialontransport.util.CollectionUtil.createLinkedHashMap;
import static java.util.Arrays.stream;
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
        final List<String> actualCellsContents = findCellsContents(actual);
        final List<String> expectedCellsContents = List.of(
                "11112222333344445555", "447736955", "10",
                "11112222333344445556", "447736956", "7",
                "11112222333344445554", "447736957", "15"
        );
        assertEquals(expectedCellsContents, actualCellsContents);
    }

    @Test
    public void headerRowCellsShouldBeCreated() {
        final AbstractCell[] actual = this.reportTableAppender.createHeaderRowCells();
        final List<String> actualCellsContents = findCellsContents(actual);
        final List<String> expectedCellsContents = List.of("Imei", "Phone number", "Count of points");
        assertEquals(expectedCellsContents, actualCellsContents);
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

    private static List<String> findCellsContents(final List<Row> rows) {
        return rows.stream()
                .flatMap(UserTrackersReportTableAppenderTest::findColumnContentsAsStream)
                .toList();
    }

    private static Stream<String> findColumnContentsAsStream(final Row row) {
        final List<AbstractCell> cells = row.getCells();
        final Stream<AbstractCell> cellStream = cells.stream();
        return findContentsAsStream(cellStream);
    }

    private static List<String> findCellsContents(final AbstractCell[] cells) {
        final Stream<AbstractCell> cellStream = stream(cells);
        return findContentsAsStream(cellStream).toList();
    }

    private static Stream<String> findContentsAsStream(final Stream<AbstractCell> cellStream) {
        return cellStream
                .map(cell -> (TextCell) cell)
                .map(TextCell::getText);
    }
}
