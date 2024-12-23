package by.bsu.wialontransport.service.report.factory;

import by.bsu.wialontransport.crud.dto.Location;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.crud.service.LocationService;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.model.DateInterval;
import by.bsu.wialontransport.model.TempTrack;
import by.bsu.wialontransport.service.report.model.TrackerMovement;
import by.bsu.wialontransport.service.report.model.UserMovementReportBuildingContext;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static by.bsu.wialontransport.util.FontFactoryUtil.loadFont;
import static java.awt.Color.WHITE;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.*;

@Component
@RequiredArgsConstructor
public final class UserMovementReportBuildingContextFactory {
    private static final String FONT_PATH = "fonts/Roboto-Regular.ttf";
//    private static final Comparator<Data> DATA_COMPARATOR_BY_DATE_TIME = comparing(Data::findDateTime);
    private static final Integer FONT_SIZE = 11;
    private static final Color BORDER_COLOR = WHITE;

    private final TrackerService trackerService;
    private final LocationService dataService;
    private final TrackFactory trackFactory;
//    private final MileageCalculatingService mileageCalculatingService;

    public UserMovementReportBuildingContext create(final User user, final DateInterval dateInterval) {
        final PDDocument document = new PDDocument();
        final PDFont font = loadFont(document, FONT_PATH);
        final List<TrackerMovement> trackerMovements = this.createTrackerMovements(user, dateInterval);
        return UserMovementReportBuildingContext.builder()
                .user(user)
                .dateInterval(dateInterval)
                .document(document)
                .font(font)
                .trackerMovements(trackerMovements)
                .fontSize(FONT_SIZE)
                .borderColor(BORDER_COLOR)
                .build();
    }

    private List<TrackerMovement> createTrackerMovements(final User user, final DateInterval dateInterval) {
        return this.findSortedByDateTimeDataGroupedByAllTrackers(user, dateInterval)
                .entrySet()
                .stream()
                .map(this::createTrackerMovement)
                .toList();
    }

    private Map<Tracker, List<Location>> findSortedByDateTimeDataGroupedByAllTrackers(final User user,
                                                                                      final DateInterval dateInterval) {
        final Map<Tracker, List<Location>> dataByTrackers = this.findSortedByDateTimeDataGroupedByTrackers(user, dateInterval);
        this.insertLackedTrackers(dataByTrackers, user);
        return dataByTrackers;
    }

    private Map<Tracker, List<Location>> findSortedByDateTimeDataGroupedByTrackers(final User user,
                                                                                   final DateInterval dateInterval) {
//        final List<Data> data = this.dataService.findDataWithTrackerAndAddress(user, dateInterval);
//        return groupSortedByDateTimeDataByTrackers(data);
        return null;
    }

    private static Map<Tracker, List<Location>> groupSortedByDateTimeDataByTrackers(final List<Location> data) {
        return data.stream()
                .collect(
                        groupingBy(
                                Location::getTracker,
                                collectingAndThen(
                                        toList(),
                                        UserMovementReportBuildingContextFactory::sortByDateTime
                                )
                        )
                );
    }

    private static List<Location> sortByDateTime(final List<Location> data) {
//        data.sort(DATA_COMPARATOR_BY_DATE_TIME);
        return data;
    }

    private void insertLackedTrackers(final Map<Tracker, List<Location>> dataByTrackers, final User user) {
//        final List<Tracker> userAllTrackers = this.trackerService.findByUser(user);
//        userAllTrackers.forEach(userTracker -> dataByTrackers.computeIfAbsent(userTracker, tracker -> emptyList()));
    }

    private TrackerMovement createTrackerMovement(final Entry<Tracker, List<Location>> dataByTracker) {
        final Tracker tracker = dataByTracker.getKey();
        final List<Location> trackerData = dataByTracker.getValue();
        final TempTrack track = this.trackFactory.create(trackerData);
//        final Mileage mileage = this.mileageCalculatingService.calculate(track);
//        return new TrackerMovement(tracker, trackerData, mileage);
        return null;
    }
}
