package by.bsu.wialontransport.service.report.model;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.model.DateInterval;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Getter
@Builder
public final class UserMovementReportBuildingContext implements AutoCloseable {
    private final User user;
    private final DateInterval dateInterval;
    private final PDDocument document;
    private final PDFont font;
    private final Map<Tracker, Integer> pointCountsByAllTrackers;
    private final Map<Tracker, List<Data>> dataGroupedBySortedByImeiTrackers;
    //TODO: add bar-chart for it
    private final Map<LocalDate, Map<Tracker, List<Data>>>

    //TODO: refactor
    public Integer getFontSize() {
        return 11;
    }

    //TODO: refactor
    public Color getBorderColor() {
        return Color.WHITE;
    }

    @Override
    public void close()
            throws IOException {
        this.document.close();
    }
}
