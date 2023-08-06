package by.bsu.wialontransport.service.report.model;

import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.model.DateInterval;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.awt.*;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Getter
@Builder
public final class UserMovementReportBuildingContext implements AutoCloseable {
    private final User user;
    private final DateInterval dateInterval;
    private final PDDocument document;
    private final PDFont font;
    private final List<TrackerMovement> trackerMovements;
    private final Integer fontSize;
    private final Color borderColor;

    @Override
    public void close()
            throws IOException {
        this.document.close();
    }
}
