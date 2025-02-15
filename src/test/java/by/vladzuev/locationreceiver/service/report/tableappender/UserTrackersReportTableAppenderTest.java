package by.vladzuev.locationreceiver.service.report.tableappender;

import by.vladzuev.locationreceiver.crud.dto.Location;
import by.vladzuev.locationreceiver.crud.dto.Tracker;
import by.vladzuev.locationreceiver.service.report.model.TrackerMovement;
import by.vladzuev.locationreceiver.util.PDFTestUtil;
import org.junit.Test;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.cell.AbstractCell;

import java.util.List;
import java.util.stream.Stream;

import static by.vladzuev.locationreceiver.util.PDFTestUtil.findCellContents;
import static org.junit.Assert.assertEquals;

public final class UserTrackersReportTableAppenderTest {
    private final UserTrackersReportTableAppender tableAppender = new UserTrackersReportTableAppender(null);

    @Test
    public void contentRowStreamShouldBeCreated() {
        final String givenTrackerImei = "11112222333344445555";
        final String givenPhoneNumber = "447336934";
        final Tracker givenTracker = createTracker(givenTrackerImei, givenPhoneNumber);

        final List<Location> givenData = List.of(createData(), createData(), createData(), createData(), createData());

        final TrackerMovement givenTrackerMovement = createTrackerMovement(givenTracker, givenData);

        final Stream<Row> actual = this.tableAppender.createContentRowStream(givenTrackerMovement);
        final List<String> actualCellContents = PDFTestUtil.findCellContents(actual);
        final List<String> expectedCellContents = List.of(
                givenTrackerImei, givenPhoneNumber, "5"
        );
        assertEquals(expectedCellContents, actualCellContents);
    }

    @Test
    public void headerRowCellsShouldBeCreated() {
        final AbstractCell[] actual = this.tableAppender.createHeaderRowCells();
        final List<String> actualCellContents = PDFTestUtil.findCellContents(actual);
        final List<String> expectedCellContents = List.of("Imei", "Phone number", "Count of points");
        assertEquals(expectedCellContents, actualCellContents);
    }

    private static Tracker createTracker(final String imei, final String phoneNumber) {
        return Tracker.builder()
                .imei(imei)
                .phoneNumber(phoneNumber)
                .build();
    }

    private static Location createData() {
        return Location.builder().build();
    }

    private static TrackerMovement createTrackerMovement(final Tracker tracker, final List<Location> data) {
        return TrackerMovement.builder()
                .tracker(tracker)
                .data(data)
                .build();
    }
}
