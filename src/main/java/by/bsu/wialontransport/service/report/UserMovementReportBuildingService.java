package by.bsu.wialontransport.service.report;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.model.DateInterval;
import by.bsu.wialontransport.service.report.exception.UserMovementReportBuildingException;
import by.bsu.wialontransport.service.report.factory.UserMovementReportBuildingContextFactory;
import by.bsu.wialontransport.service.report.model.DistributedTable;
import by.bsu.wialontransport.service.report.model.ReportTable;
import by.bsu.wialontransport.service.report.model.UserMovementReportBuildingContext;
import by.bsu.wialontransport.service.report.tablebuilder.DistributedUserMovementTableBuilder;
import by.bsu.wialontransport.service.report.tablebuilder.DistributedUserTrackersTableBuilder;
import by.bsu.wialontransport.service.report.tabledrawer.DistributedReportTableDrawer;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.springframework.stereotype.Service;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static by.bsu.wialontransport.util.CellFactoryUtil.createTextCell;
import static by.bsu.wialontransport.util.PDFUtil.addPage;
import static java.lang.String.format;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public final class UserMovementReportBuildingService {
    private static final String DATE_PATTERN = "dd.MM.yyyy";
    private static final DateTimeFormatter DATE_FORMATTER = ofPattern(DATE_PATTERN);

    private static final float INTRODUCTION_PAGE_FONT_SIZE = 16;

    private static final String REPORT_NAME = "Movement report";
    private static final String TEMPLATE_ROW_WITH_USER_EMAIL = "User's email: %s";
    private static final String TEMPLATE_ROW_WITH_DATE_INTERVAL = "Date interval: %s - %s";

    private static final float INTRODUCTION_CONTENT_LINES_LEADING = 14.5f;
    private static final float INTRODUCTION_NEW_LINE_AT_OFFSET_X = 25;
    private static final float INTRODUCTION_NEW_LINE_AT_OFFSET_Y = 750;

    private final UserMovementReportBuildingContextFactory contextFactory;
    private final DistributedReportTableDrawer tableDrawer;

    public byte[] createReport(final User user, final DateInterval dateInterval) {
        try (final UserMovementReportBuildingContext context = this.contextFactory.create(user, dateInterval)) {
            addIntroduction(context);
            this.addUserTrackersTable(context);
            this.addUserMovementTable(context);
            return transformToByteArray(context.getDocument());
        } catch (final IOException cause) {
            throw new UserMovementReportBuildingException(cause);
        }
    }

    private static void addIntroduction(final UserMovementReportBuildingContext context)
            throws IOException {
        final PDDocument document = context.getDocument();
        final PDPage page = addPage(document);
        try (final PDPageContentStream pageContentStream = new PDPageContentStream(document, page)) {
            addIntroductionContentLines(pageContentStream, context);
        }
    }

    private static void addIntroductionContentLines(final PDPageContentStream pageContentStream,
                                                    final UserMovementReportBuildingContext context)
            throws IOException {
        final String rowWithUserEmail = createRowWithUserEmail(context.getUser());
        final String rowWithDateInterval = createRowWithDateInterval(context.getDateInterval());
        addIntroductionContentLines(
                pageContentStream,
                context.getFont(),
                REPORT_NAME,
                rowWithUserEmail,
                rowWithDateInterval
        );
    }

    private static String createRowWithUserEmail(final User user) {
        return format(TEMPLATE_ROW_WITH_USER_EMAIL, user.getEmail());
    }

    private static String createRowWithDateInterval(final DateInterval dateInterval) {
        final String formattedStartDate = DATE_FORMATTER.format(dateInterval.getStart());
        final String formattedEndDate = DATE_FORMATTER.format(dateInterval.getEnd());
        return format(TEMPLATE_ROW_WITH_DATE_INTERVAL, formattedStartDate, formattedEndDate);
    }

    private static void addIntroductionContentLines(final PDPageContentStream pageContentStream,
                                                    final PDFont font,
                                                    final String... lines)
            throws IOException {
        pageContentStream.setLeading(INTRODUCTION_CONTENT_LINES_LEADING);
        pageContentStream.beginText();
        pageContentStream.setFont(font, INTRODUCTION_PAGE_FONT_SIZE);
        pageContentStream.newLineAtOffset(INTRODUCTION_NEW_LINE_AT_OFFSET_X, INTRODUCTION_NEW_LINE_AT_OFFSET_Y);
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

    private static final float TABLE_COLUMN_WIDTH_OF_IMEI = 150;
    private static final float TABLE_COLUMN_WIDTH_OF_PHONE_NUMBER = 150;
    private static final float TABLE_COLUMN_WIDTH_OF_COUNT_OF_POINTS = 150;
    private static final float[] TABLE_COLUMNS_WIDTHS = {
            TABLE_COLUMN_WIDTH_OF_IMEI,
            TABLE_COLUMN_WIDTH_OF_PHONE_NUMBER,
            TABLE_COLUMN_WIDTH_OF_COUNT_OF_POINTS
    };

    private void addUserTrackersTable(final UserMovementReportBuildingContext context) {
        final DistributedTable table = buildDistributedUserTrackersTable(context);
        final ReportTable reportTable = new ReportTable(
                table.getPageTables().stream().map(Table::getRows).flatMap(Collection::stream).toList(),
                TABLE_COLUMNS_WIDTHS,
                context.getFont(),
                25,
                Color.blue
        );
        this.tableDrawer.draw(reportTable, context.getDocument());
    }

    private static DistributedTable buildDistributedUserTrackersTable(final UserMovementReportBuildingContext context) {
        final DistributedUserTrackersTableBuilder tableBuilder = new DistributedUserTrackersTableBuilder(
                context.getFont()
        );
        context.getDataGroupedBySortedByImeiTrackers()
                .forEach(
                        (tracker, trackerData) -> tableBuilder.addRow(
                                createUserTrackersTableRow(tracker, trackerData)
                        )
                );
        return tableBuilder.build();
    }

    private static Row createUserTrackersTableRow(final Tracker tracker, final List<Data> trackerData) {
        final String trackerImei = tracker.getImei();
        final String trackerPhoneNumber = tracker.getPhoneNumber();
        final int countOfPoints = trackerData.size();
        return Row.builder()
                .add(createTextCell(trackerImei))
                .add(createTextCell(trackerPhoneNumber))
                .add(createTextCell(countOfPoints))
                .build();
    }

    private static final float TABLE_COLUMN_WIDTH_OF_DATETIME = 110;
    private static final float TABLE_COLUMN_WIDTH_OF_LATITUDE = 110;
    private static final float TABLE_COLUMN_WIDTH_OF_LONGITUDE = 110;
    private static final float TABLE_COLUMN_WIDTH_OF_CITY = 110;
    private static final float TABLE_COLUMN_WIDTH_OF_COUNTRY = 110;
    private static final float[] TABLE_COLUMNS_WIDTHS_2 = {
            TABLE_COLUMN_WIDTH_OF_DATETIME,
            TABLE_COLUMN_WIDTH_OF_LATITUDE,
            TABLE_COLUMN_WIDTH_OF_LONGITUDE,
            TABLE_COLUMN_WIDTH_OF_CITY,
            TABLE_COLUMN_WIDTH_OF_COUNTRY
    };

    private void addUserMovementTable(final UserMovementReportBuildingContext context) {
        final DistributedTable table = buildDistributedUserMovementTable(
                context.getDataGroupedBySortedByImeiTrackers(),
                context.getFont()
        );
        final ReportTable reportTable = new ReportTable(
                table.getPageTables().stream().map(Table::getRows).flatMap(Collection::stream).toList(),
                TABLE_COLUMNS_WIDTHS_2,
                context.getFont(),
                25,
                Color.blue
        );
        this.tableDrawer.draw(reportTable, context.getDocument());
    }

    private static DistributedTable buildDistributedUserMovementTable(final Map<Tracker, List<Data>> dataGroupedByTrackers,
                                                                      final PDFont font) {
        return dataGroupedByTrackers.entrySet()
                .stream()
                .map(entry -> buildDistributedUserMovementTable(entry, font))
                .collect(collectingAndThen(toList(), DistributedTable::unite));
    }

    private static DistributedTable buildDistributedUserMovementTable(final Entry<Tracker, List<Data>> dataByTracker,
                                                                      final PDFont font) {
        final Tracker tracker = dataByTracker.getKey();
        final DistributedUserMovementTableBuilder tableBuilder = new DistributedUserMovementTableBuilder(font, tracker);
        dataByTracker.getValue().forEach(data -> tableBuilder.addRow(createUserMovementTableRow(data)));
        return tableBuilder.build();
    }

    private static Row createUserMovementTableRow(final Data data) {
        final LocalDateTime dateTime = data.findDateTime();
        final double latitudeAsDouble = data.findLatitudeAsDouble();
        final double longitudeAsDouble = data.findLongitudeAsDouble();
        final String cityName = data.findCityName();
        final String countryName = data.findCountryName();
        return Row.builder()
                .add(createTextCell(dateTime))
                .add(createTextCell(latitudeAsDouble))
                .add(createTextCell(longitudeAsDouble))
                .add(createTextCell(cityName))
                .add(createTextCell(countryName))
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
