package by.bsu.wialontransport.service.report.model;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.model.DateInterval;
import by.bsu.wialontransport.model.Mileage;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.awt.*;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
@Getter
@Builder
public final class UserMovementReportBuildingContext implements AutoCloseable {
    private static final Comparator<Tracker> COMPARATOR_TO_SORT_TRACKER_BY_IMEI = comparing(Tracker::getImei);

    private final User user;
    private final DateInterval dateInterval;
    private final PDDocument document;
    private final PDFont font;
    private final List<TrackerMovement> trackerMovements;

    //TODO: refactor
    public Integer getFontSize() {
        return 11;
    }

    //TODO: refactor
    public Color getBorderColor() {
        return Color.WHITE;
    }

    //TODO: transfer in suitable table appender
    public Map<Tracker, List<Data>> findPointsBySortedByImeiTrackers() {
        return this.trackerMovements.stream()
                .collect(
                        toMap(
                                TrackerMovement::getTracker,
                                TrackerMovement::getData,
                                //TODO: throw exception
                                (existing, replacement) -> existing,
                                UserMovementReportBuildingContext::createTreeMapWithKeyAsTrackerSortedByImei
                        )
                );
    }

    public Map<Tracker, Mileage> findMileagesBySortedByImeiTrackers() {
        return this.trackerMovements.stream()
                .collect(
                        toMap(
                                TrackerMovement::getTracker,
                                TrackerMovement::getMileage,
                                //TODO: throw exception
                                (existing, replacement) -> existing,
                                UserMovementReportBuildingContext::createTreeMapWithKeyAsTrackerSortedByImei
                        )
                );
    }

    @Override
    public void close()
            throws IOException {
        this.document.close();
    }

    private static <V> Map<Tracker, V> createTreeMapWithKeyAsTrackerSortedByImei() {
        return new TreeMap<>(COMPARATOR_TO_SORT_TRACKER_BY_IMEI);
    }
}
