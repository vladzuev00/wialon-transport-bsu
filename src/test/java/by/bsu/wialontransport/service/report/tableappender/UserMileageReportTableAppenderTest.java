package by.bsu.wialontransport.service.report.tableappender;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.model.Mileage;
import by.bsu.wialontransport.service.report.model.TrackerMovement;
import org.junit.Test;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.cell.AbstractCell;

import java.util.List;
import java.util.stream.Stream;

import static by.bsu.wialontransport.util.PDFTestUtil.findCellContents;
import static org.junit.Assert.assertEquals;

public final class UserMileageReportTableAppenderTest {
    private final UserMileageReportTableAppender tableAppender = new UserMileageReportTableAppender(null);

    @Test
    public void contentRowStreamShouldBeCreated() {
        final String givenTrackerImei = "11112222333344445555";
        final Tracker givenTracker = createTracker(givenTrackerImei);

        final Mileage givenMileage = new Mileage(5.5, 6.6);

        final TrackerMovement givenTrackerMovement = createTrackerMovement(givenTracker, givenMileage);

        final Stream<Row> actual = this.tableAppender.createContentRowStream(givenTrackerMovement);
        final List<String> actualCellContents = findCellContents(actual);
        final List<String> expectedCellContents = List.of(givenTrackerImei, "5.5", "6.6", "12.1");
        assertEquals(expectedCellContents, actualCellContents);
    }

    @Test
    public void headerRowCellsShouldBeCreated() {
        final AbstractCell[] actual = this.tableAppender.createHeaderRowCells();
        final List<String> actualCellContents = findCellContents(actual);
        final List<String> expectedCellContents = List.of(
                "Tracker's imei", "Urban mileage", "Country's mileage", "Total mileage"
        );
        assertEquals(expectedCellContents, actualCellContents);
    }

    private static Tracker createTracker(final String imei) {
        return Tracker.builder()
                .imei(imei)
                .build();
    }

    private static TrackerMovement createTrackerMovement(final Tracker tracker, final Mileage mileage) {
        return TrackerMovement.builder()
                .tracker(tracker)
                .mileage(mileage)
                .build();
    }
}