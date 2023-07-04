package by.bsu.wialontransport.service.report;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.crud.service.DataService;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.model.DateInterval;
import by.bsu.wialontransport.service.report.exception.UserMovementReportBuildingException;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.springframework.stereotype.Service;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.Table.TableBuilder;
import org.vandeseer.easytable.structure.cell.TextCell;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static java.awt.Color.WHITE;
import static java.lang.String.format;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.groupingBy;
import static org.apache.pdfbox.pdmodel.font.PDType1Font.*;
import static org.vandeseer.easytable.settings.HorizontalAlignment.CENTER;

@Service
@RequiredArgsConstructor
public final class UserMovementReportBuildingService {
    private static final PDFont FIRST_PAGE_FONT = TIMES_ROMAN;
    private static final float FIRST_PAGE_FONT_SIZE = 16;

    private static final String REPORT_NAME = "Movement report";
    private static final String TEMPLATE__ROW_WITH_USER_EMAIL = "User's email: %s";
    private static final String TEMPLATE_ROW_WITH_DATE_INTERVAL = "Date interval: %s - %s";

    private static final String DATE_PATTERN = "dd.MM.yyyy";
    private static final DateTimeFormatter FORMATTER = ofPattern(DATE_PATTERN);

    private static final float INTRODUCTION_CONTENT_LINES_LEADING = 14.5f;
    private static final float INTRODUCTION_NEW_LINE_AT_OFFSET_X = 25;
    private static final float INTRODUCTION_NEW_LINE_AT_OFFSET_Y = 750;

    private static final float INTRODUCTION_TABLE_COLUMN_WIDTH_OF_IMEI = 200;
    private static final float INTRODUCTION_TABLE_COLUMN_WIDTH_OF_PHONE_NUMBER = 150;
    private static final float INTRODUCTION_TABLE_COLUMN_WIDTH_OF_COUNT_OF_POINTS = 100;
    private static final float[] INTRODUCTION_TABLE_COLUMNS_WIDTHS = {
            INTRODUCTION_TABLE_COLUMN_WIDTH_OF_IMEI,
            INTRODUCTION_TABLE_COLUMN_WIDTH_OF_PHONE_NUMBER,
            INTRODUCTION_TABLE_COLUMN_WIDTH_OF_COUNT_OF_POINTS
    };

    private static final Integer INTRODUCTION_TABLE_FONT_SIZE = 10;
    private static final PDFont INTRODUCTION_TABLE_FONT = HELVETICA;
    private static final Color INTRODUCTION_TABLE_BORDER_COLOR = WHITE;

    private final TrackerService trackerService;
    private final DataService dataService;

    public byte[] createReport(final User user, final DateInterval dateInterval) {
        try (final PDDocument document = new PDDocument()) {
            addIntroduction(document, user, dateInterval);



            return transformToByteArray(document);
        } catch (final IOException cause) {
            throw new UserMovementReportBuildingException(cause);
        }
    }

    //TODO: 2 запроса объединить в один
    private void addIntroduction(final PDDocument document, final User user, final DateInterval dateInterval)
            throws IOException {
        final PDPage page = addPage(document);
        try (final PDPageContentStream pageContentStream = new PDPageContentStream(document, page)) {
            addIntroductionContentLines(pageContentStream, user, dateInterval);

            final TableBuilder tableBuilder = Table.builder()
                    .addColumnsOfWidth(INTRODUCTION_TABLE_COLUMNS_WIDTHS)
                    .fontSize(INTRODUCTION_TABLE_FONT_SIZE)
                    .font(INTRODUCTION_TABLE_FONT)
                    .borderColor(INTRODUCTION_TABLE_BORDER_COLOR)
                    //header row
                    .addRow(Row.builder()
                            .add(TextCell.builder().text("Imei").borderWidth(1).build())
                            .add(TextCell.builder().text("Phone number").borderWidth(1).build())
                            .add(TextCell.builder().text("Count of points").borderWidth(1).build())
                            .backgroundColor(new Color(76, 129, 190))
                            .textColor(WHITE)
                            .font(HELVETICA_BOLD)
                            .fontSize(11)
                            .horizontalAlignment(CENTER)
                            .build());

            final List<Tracker> userTrackers = this.trackerService.findByUser(user);
            final List<Data> data = this.dataService.findDataWithTrackerAndAddress(user, dateInterval);
            final Map<Tracker, List<Data>> dataGroupedByTrackers = data.stream().collect(groupingBy(Data::getTracker));
            userTrackers.forEach(userTracker -> dataGroupedByTrackers.computeIfAbsent(userTracker, tracker -> emptyList()));
            dataGroupedByTrackers.keySet()
                    .forEach(tracker -> tableBuilder.addRow(Row.builder()
                            .add(TextCell.builder().text(tracker.getImei()).borderWidth(1).build())
                            .add(TextCell.builder().text(tracker.getPhoneNumber()).borderWidth(1).build())
                            .add(TextCell.builder().text(Integer.toString(dataGroupedByTrackers.get(tracker).size())).borderWidth(1).build())
                            .build()));

            final Table table = tableBuilder.build();

            TableDrawer.builder()
                    .contentStream(pageContentStream)
                    .table(table)
                    .startX(25)
                    .startY(650)
                    .build()
                    .draw();
        }
    }

    private static void addIntroductionContentLines(final PDPageContentStream pageContentStream,
                                                    final User user,
                                                    final DateInterval dateInterval)
            throws IOException {
        final String rowWithUserEmail = createRowWithUserEmail(user);
        final String rowWithDateInterval = createRowWithDateInterval(dateInterval);
        addContentLines(
                pageContentStream,
                INTRODUCTION_CONTENT_LINES_LEADING,
                INTRODUCTION_NEW_LINE_AT_OFFSET_X,
                INTRODUCTION_NEW_LINE_AT_OFFSET_Y,
                REPORT_NAME,
                rowWithUserEmail,
                rowWithDateInterval
        );
    }

    private static String createRowWithUserEmail(final User user) {
        return format(TEMPLATE__ROW_WITH_USER_EMAIL, user.getEmail());
    }

    private static String createRowWithDateInterval(final DateInterval dateInterval) {
        final String formattedStartDate = FORMATTER.format(dateInterval.getStart());
        final String formattedEndDate = FORMATTER.format(dateInterval.getEnd());
        return format(TEMPLATE_ROW_WITH_DATE_INTERVAL, formattedStartDate, formattedEndDate);
    }

    private static void addContentLines(final PDPageContentStream pageContentStream,
                                        final float leading,
                                        final float newLineAtOffsetX,
                                        final float newLineAtOffsetY,
                                        final String... lines)
            throws IOException {
        pageContentStream.setLeading(leading);
        pageContentStream.beginText();
        pageContentStream.newLineAtOffset(newLineAtOffsetX, newLineAtOffsetY);
        pageContentStream.setFont(FIRST_PAGE_FONT, FIRST_PAGE_FONT_SIZE);
        addLines(pageContentStream, lines);
        pageContentStream.endText();
    }

    private static void addLines(final PDPageContentStream pageContentStream, final String... lines) {
        stream(lines).forEach(line -> addLine(pageContentStream, line));
    }

    private static void addLine(final PDPageContentStream pageContentStream, final String line) {
        try {
            pageContentStream.showText(line);
            pageContentStream.newLine();
        } catch (final IOException cause) {
            throw new UserMovementReportBuildingException(cause);
        }
    }

    private static PDPage addPage(final PDDocument document) {
        final PDPage page = new PDPage();
        document.addPage(page);
        return page;
    }

    private static byte[] transformToByteArray(final PDDocument document)
            throws IOException {
        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            document.save(outputStream);
            return outputStream.toByteArray();
        }
    }
}
