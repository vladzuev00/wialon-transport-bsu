package by.bsu.wialontransport.service.report;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.crud.service.DataService;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.model.DateInterval;
import by.bsu.wialontransport.service.report.exception.UserMovementReportBuildingException;
import by.bsu.wialontransport.service.report.factory.ReportBuildingContextFactory;
import by.bsu.wialontransport.service.report.model.ReportBuildingContext;
import by.bsu.wialontransport.service.report.tablebuilder.DistributedUserMovementTableBuilder;
import by.bsu.wialontransport.service.report.tablebuilder.DistributedUserTrackersTableBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.springframework.stereotype.Service;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Supplier;

import static by.bsu.wialontransport.util.CellFactoryUtil.createTextCell;
import static by.bsu.wialontransport.util.FontFactoryUtil.loadFont;
import static java.lang.String.format;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public final class UserMovementReportBuildingService {
    private static final String DATE_PATTERN = "dd.MM.yyyy";
    private static final DateTimeFormatter DATE_FORMATTER = ofPattern(DATE_PATTERN);

    private static final float FIRST_PAGE_FONT_SIZE = 16;

    private static final String REPORT_NAME = "Movement report";
    private static final String TEMPLATE__ROW_WITH_USER_EMAIL = "User's email: %s";
    private static final String TEMPLATE_ROW_WITH_DATE_INTERVAL = "Date interval: %s - %s";

    private static final float INTRODUCTION_CONTENT_LINES_LEADING = 14.5f;
    private static final float INTRODUCTION_NEW_LINE_AT_OFFSET_X = 25;
    private static final float INTRODUCTION_NEW_LINE_AT_OFFSET_Y = 750;

    private static final float PAGE_TABLE_START_X = 75F;
    private static final float PAGE_TABLE_START_Y = 650F;

    private final ReportBuildingContextFactory contextFactory;

    public byte[] createReport(final User user, final DateInterval dateInterval) {
        try (final ReportBuildingContext context = this.contextFactory.create(user, dateInterval)) {
            addIntroduction(context);
            addUserTrackersTable(dataGroupedByTrackers, document, font);
            addUserMovementTable(dataGroupedByTrackers, document, font);
            return transformToByteArray(document);
        } catch (final IOException cause) {
            throw new UserMovementReportBuildingException(cause);
        }
    }

    private static void addIntroduction(final ReportBuildingContext context)
            throws IOException {
        final PDDocument document = context.getDocument();
        final PDPage page = addPage(document);
        try (final PDPageContentStream pageContentStream = new PDPageContentStream(document, page)) {
            addIntroductionContentLines(pageContentStream, user, dateInterval, font);
        }
    }

    private static PDPage addPage(final PDDocument document) {
        final PDPage page = new PDPage();
        document.addPage(page);
        return page;
    }

    private static void addIntroductionContentLines(final PDPageContentStream pageContentStream,
                                                    final User user,
                                                    final DateInterval dateInterval,
                                                    final PDFont font)
            throws IOException {
        final String rowWithUserEmail = createRowWithUserEmail(user);
        final String rowWithDateInterval = createRowWithDateInterval(dateInterval);
        addIntroductionContentLines(
                pageContentStream,
                font,
                REPORT_NAME,
                rowWithUserEmail,
                rowWithDateInterval
        );
    }

    private static String createRowWithUserEmail(final User user) {
        return format(TEMPLATE__ROW_WITH_USER_EMAIL, user.getEmail());
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
        pageContentStream.setFont(font, FIRST_PAGE_FONT_SIZE);
        pageContentStream.newLineAtOffset(INTRODUCTION_NEW_LINE_AT_OFFSET_X, INTRODUCTION_NEW_LINE_AT_OFFSET_Y);
        addLines(pageContentStream, lines);
        pageContentStream.endText();
    }

    private static void addLines(final PDPageContentStream pageContentStream, final String... lines) {
        stream(lines)
                .forEach(line -> addLine(pageContentStream, line));
    }

    private static void addLine(final PDPageContentStream pageContentStream, final String line) {
        try {
            pageContentStream.showText(line);
            pageContentStream.newLine();
        } catch (final IOException cause) {
            throw new UserMovementReportBuildingException(cause);
        }
    }

    private static void addUserTrackersTable(final Map<Tracker, List<Data>> dataGroupedByTrackers,
                                             final PDDocument document,
                                             final PDFont font) {
        final List<Table> pageTables = buildDistributedUserTrackersTable(dataGroupedByTrackers, font);
        drawPageTables(document, pageTables);
    }

    private static List<Table> buildDistributedUserTrackersTable(final Map<Tracker, List<Data>> dataGroupedByTrackers,
                                                                 final PDFont font) {
        final DistributedUserTrackersTableBuilder tableBuilder = new DistributedUserTrackersTableBuilder(font);
        dataGroupedByTrackers
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

    private static void drawPageTables(final PDDocument document, final List<Table> pageTables) {
        pageTables.forEach(table -> drawPageTable(document, table));
    }

    private static void drawPageTable(final PDDocument document, final Table pageTable) {
        final PDPage page = addPage(document);
        try (final PDPageContentStream pageContentStream = new PDPageContentStream(document, page)) {
            drawPageTable(pageContentStream, pageTable);
        } catch (final IOException cause) {
            throw new UserMovementReportBuildingException(cause);
        }
    }

    private static void drawPageTable(final PDPageContentStream pageContentStream, final Table pageTable) {
        TableDrawer.builder()
                .contentStream(pageContentStream)
                .table(pageTable)
                .startX(PAGE_TABLE_START_X)
                .startY(PAGE_TABLE_START_Y)
                .build()
                .draw();
    }

    private static void addUserMovementTable(final Map<Tracker, List<Data>> dataGroupedByTrackers,
                                             final PDDocument document,
                                             final PDFont font) {
        final List<Table> pageTables = buildDistributedUserMovementTables(dataGroupedByTrackers, font);
        drawPageTables(document, pageTables);
    }

    private static List<Table> buildDistributedUserMovementTables(final Map<Tracker, List<Data>> dataGroupedByTrackers,
                                                                  final PDFont font) {
        return dataGroupedByTrackers.entrySet()
                .stream()
                .map(entry -> buildDistributedUserMovementTable(entry, font))
                .flatMap(Collection::stream)
                .toList();
    }

    private static List<Table> buildDistributedUserMovementTable(final Entry<Tracker, List<Data>> dataByTracker,
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
