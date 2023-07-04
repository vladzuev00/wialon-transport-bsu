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
import org.vandeseer.easytable.settings.HorizontalAlignment;
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

import static java.awt.Color.*;
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

    private static final float INTRODUCTION_TABLE_COLUMN_WIDTH_OF_IMEI = 150;
    private static final float INTRODUCTION_TABLE_COLUMN_WIDTH_OF_PHONE_NUMBER = 150;
    private static final float INTRODUCTION_TABLE_COLUMN_WIDTH_OF_COUNT_OF_POINTS = 150;
    private static final float[] INTRODUCTION_TABLE_COLUMNS_WIDTHS = {
            INTRODUCTION_TABLE_COLUMN_WIDTH_OF_IMEI,
            INTRODUCTION_TABLE_COLUMN_WIDTH_OF_PHONE_NUMBER,
            INTRODUCTION_TABLE_COLUMN_WIDTH_OF_COUNT_OF_POINTS
    };

    private static final Integer INTRODUCTION_TABLE_FONT_SIZE = 10;
    private static final PDFont INTRODUCTION_TABLE_FONT = HELVETICA;
    private static final Color INTRODUCTION_TABLE_BORDER_COLOR = WHITE;

    private static final String INTRODUCTION_TABLE_HEADER_COLUMN_OF_IMEI_NAME = "Imei";
    private static final String INTRODUCTION_TABLE_HEADER_COLUMN_OF_PHONE_NUMBER_NAME = "Phone number";
    private static final String INTRODUCTION_TABLE_HEADER_COLUMN_OF_COUNT_OF_POINTS_NAME = "Count of points";

    private static final Color INTRODUCTION_TABLE_HEADER_ROW_BACKGROUND_COLOR = BLUE;
    private static final Color INTRODUCTION_TABLE_HEADER_ROW_TEXT_COLOR = WHITE;
    private static final PDFont INTRODUCTION_TABLE_HEADER_ROW_FONT = HELVETICA_BOLD;
    private static final Integer INTRODUCTION_TABLE_HEADER_ROW_FONT_SIZE = 11;
    private static final HorizontalAlignment INTRODUCTION_TABLE_HEADER_ROW_HORIZONTAL_ALIGNMENT = CENTER;

    private static final float INTRODUCTION_TABLE_START_X = 25F;
    private static final float INTRODUCTION_TABLE_START_Y = 650F;

    private static final float CELL_BORDER_WIDTH = 1;

    private final TrackerService trackerService;
    private final DataService dataService;

    public byte[] createReport(final User user, final DateInterval dateInterval) {
        try (final PDDocument document = new PDDocument()) {
            final Map<Tracker, List<Data>> dataGroupedByTrackers = this.findDataGroupedByAllTrackersOfUser(
                    user, dateInterval
            );
            this.addIntroduction(document, user, dateInterval, dataGroupedByTrackers);
            return transformToByteArray(document);
        } catch (final IOException cause) {
            throw new UserMovementReportBuildingException(cause);
        }
    }

    //TODO: 2 запроса объединить в один
    private Map<Tracker, List<Data>> findDataGroupedByAllTrackersOfUser(final User user,
                                                                        final DateInterval dateInterval) {
        final List<Tracker> userTrackers = this.trackerService.findByUser(user);
        final List<Data> data = this.dataService.findDataWithTrackerAndAddress(user, dateInterval);
        final Map<Tracker, List<Data>> dataGroupedByTrackers = groupDataByTrackers(data);
        userTrackers.forEach(
                userTracker -> dataGroupedByTrackers.computeIfAbsent(userTracker, tracker -> emptyList())
        );
        return dataGroupedByTrackers;
    }

    private static Map<Tracker, List<Data>> groupDataByTrackers(final List<Data> data) {
        return data.stream()
                .collect(groupingBy(Data::getTracker));
    }

    //introduction includes report's name, user's email, date interval, table with trackers
    private void addIntroduction(final PDDocument document,
                                 final User user,
                                 final DateInterval dateInterval,
                                 final Map<Tracker, List<Data>> dataGroupedByTrackers)
            throws IOException {
        final PDPage page = addPage(document);
        try (final PDPageContentStream pageContentStream = new PDPageContentStream(document, page)) {
            addIntroductionContentLines(pageContentStream, user, dateInterval);
            addIntroductionTable(dataGroupedByTrackers, pageContentStream);
        }
    }

    private static PDPage addPage(final PDDocument document) {
        final PDPage page = new PDPage();
        document.addPage(page);
        return page;
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

    private static void addIntroductionTable(final Map<Tracker, List<Data>> dataGroupedByTrackers,
                                             final PDPageContentStream pageContentStream) {
        final Table table = buildIntroductionTable(dataGroupedByTrackers);
        drawIntroductionTable(pageContentStream, table);
    }

    private static Table buildIntroductionTable(final Map<Tracker, List<Data>> dataGroupedByTrackers) {
        final TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth(INTRODUCTION_TABLE_COLUMNS_WIDTHS)
                .fontSize(INTRODUCTION_TABLE_FONT_SIZE)
                .font(INTRODUCTION_TABLE_FONT)
                .borderColor(INTRODUCTION_TABLE_BORDER_COLOR)
                .addRow(Row.builder().add(
                        TextCell.builder()
                                .colSpan(3)
                                .font(INTRODUCTION_TABLE_HEADER_ROW_FONT)
                                .fontSize(INTRODUCTION_TABLE_HEADER_ROW_FONT_SIZE)
                                .backgroundColor(INTRODUCTION_TABLE_HEADER_ROW_BACKGROUND_COLOR)
                                .textColor(INTRODUCTION_TABLE_HEADER_ROW_TEXT_COLOR)
                                .horizontalAlignment(INTRODUCTION_TABLE_HEADER_ROW_HORIZONTAL_ALIGNMENT)
                                .text("Trackers")
                                .build()
                        )
                        .build())
                .addRow(createIntroductionTableHeaderRow());
        dataGroupedByTrackers
                .forEach(
                        (tracker, trackerData) -> tableBuilder.addRow(
                                createIntroductionTableTrackerRow(tracker, trackerData)
                        )
                );
        return tableBuilder.build();
    }

    private static void drawIntroductionTable(final PDPageContentStream pageContentStream, final Table table) {
        TableDrawer.builder()
                .contentStream(pageContentStream)
                .table(table)
                .startX(INTRODUCTION_TABLE_START_X)
                .startY(INTRODUCTION_TABLE_START_Y)
                .build()
                .draw();
    }

    private static Row createIntroductionTableHeaderRow() {
        return Row.builder()
                .add(createCell(INTRODUCTION_TABLE_HEADER_COLUMN_OF_IMEI_NAME))
                .add(createCell(INTRODUCTION_TABLE_HEADER_COLUMN_OF_PHONE_NUMBER_NAME))
                .add(createCell(INTRODUCTION_TABLE_HEADER_COLUMN_OF_COUNT_OF_POINTS_NAME))
                .backgroundColor(INTRODUCTION_TABLE_HEADER_ROW_BACKGROUND_COLOR)
                .textColor(INTRODUCTION_TABLE_HEADER_ROW_TEXT_COLOR)
                .font(INTRODUCTION_TABLE_HEADER_ROW_FONT)
                .fontSize(INTRODUCTION_TABLE_HEADER_ROW_FONT_SIZE)
                .horizontalAlignment(INTRODUCTION_TABLE_HEADER_ROW_HORIZONTAL_ALIGNMENT)
                .build();
    }

    private static Row createIntroductionTableTrackerRow(final Tracker tracker, final List<Data> trackerData) {
        return Row.builder()
                .add(createCell(tracker.getImei()))
                .add(createCell(tracker.getPhoneNumber()))
                .add(createCell(trackerData.size()))
                .build();
    }

    private static TextCell createCell(final int content) {
        final String contentAsString = Integer.toString(content);
        return createCell(contentAsString);
    }

    private static TextCell createCell(final String content) {
        return TextCell.builder()
                .text(content)
                .borderWidth(CELL_BORDER_WIDTH)
                .build();
    }

    private static byte[] transformToByteArray(final PDDocument document)
            throws IOException {
        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            document.save(outputStream);
            return outputStream.toByteArray();
        }
    }
}
