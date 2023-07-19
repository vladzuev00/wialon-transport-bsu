package by.bsu.wialontransport.service.report.tableappender;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.service.report.model.UserMovementReportBuildingContext;
import org.junit.Test;
import org.vandeseer.easytable.structure.Row;

import java.util.List;
import java.util.Map;

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
        final List<Row> expected = List.of(
                createContentRow("11112222333344445555", "447736955", 10),
                createContentRow("11112222333344445556", "447736956", 7),
                createContentRow("11112222333344445554", "447736957", 15)
        );
        assertEquals(expected, actual);
    }

    @Test
    public void headerRowCellsShouldBeCreated() {
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
