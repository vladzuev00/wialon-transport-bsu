package by.bsu.wialontransport.service.report.factory;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.crud.service.DataService;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.model.DateInterval;
import by.bsu.wialontransport.service.report.model.UserMovementReportBuildingContext;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

import static by.bsu.wialontransport.util.FontFactoryUtil.loadFont;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
public final class UserMovementReportBuildingContextFactory {
    private static final String FONT_PATH = "fonts/Roboto-Regular.ttf";
    private static final Comparator<Tracker> COMPARATOR_TO_SORT_TRACKER_BY_IMEI = comparing(Tracker::getImei);
    private static final Supplier<Map<Tracker, List<Data>>> MAP_FACTORY_OF_DATA_GROUPED_BY_SORTED_BY_IMEI_TRACKERS
            = () -> new TreeMap<>(COMPARATOR_TO_SORT_TRACKER_BY_IMEI);

    private final TrackerService trackerService;
    private final DataService dataService;

    public UserMovementReportBuildingContext create(final User user, final DateInterval dateInterval) {
        final PDDocument document = new PDDocument();
        final PDFont font = loadFont(document, FONT_PATH);
        final Map<Tracker, List<Data>> dataGroupedBySortedByImeiTrackers = this.findDataGroupedBySortedByImeiTrackers(
                user, dateInterval
        );
        final Map<Tracker, Integer> pointCountsByAllTrackers = this.findPointCountsByAllTrackers(
                dataGroupedBySortedByImeiTrackers, user
        );
        return UserMovementReportBuildingContext.builder()
                .user(user)
                .dateInterval(dateInterval)
                .document(document)
                .font(font)
                .pointCountsByAllTrackers(pointCountsByAllTrackers)
                .dataBySortedByImeiTrackers(dataGroupedBySortedByImeiTrackers)
                .build();
    }

    private Map<Tracker, List<Data>> findDataGroupedBySortedByImeiTrackers(final User user,
                                                                           final DateInterval dateInterval) {
        final List<Data> data = this.dataService.findDataWithTrackerAndAddress(user, dateInterval);
        return groupDataBySortedByImeiTrackers(data);
    }

    private static Map<Tracker, List<Data>> groupDataBySortedByImeiTrackers(final List<Data> data) {
        return data.stream()
                .collect(
                        groupingBy(
                                Data::getTracker,
                                MAP_FACTORY_OF_DATA_GROUPED_BY_SORTED_BY_IMEI_TRACKERS,
                                toList()
                        )
                );
    }

    private Map<Tracker, Integer> findPointCountsByAllTrackers(final Map<Tracker, List<Data>> dataGroupedBySortedByImeiTrackers,
                                                               final User user) {
        final Map<Tracker, Integer> result = new TreeMap<>(COMPARATOR_TO_SORT_TRACKER_BY_IMEI);
        dataGroupedBySortedByImeiTrackers.forEach((tracker, data) -> result.put(tracker, data.size()));
        this.insertTrackersWithoutData(result, user);
        return result;
    }

    private void insertTrackersWithoutData(final Map<Tracker, Integer> pointCountsByAllTrackers, final User user) {
        final List<Tracker> userTrackers = this.trackerService.findByUser(user);
        userTrackers.forEach(
                userTracker -> pointCountsByAllTrackers.computeIfAbsent(
                        userTracker, tracker -> 0
                )
        );
    }
}
