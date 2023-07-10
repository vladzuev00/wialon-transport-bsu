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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.awt.Color.*;
import static java.lang.String.format;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.groupingBy;
import static org.apache.pdfbox.pdmodel.font.PDType1Font.*;
import static org.vandeseer.easytable.settings.HorizontalAlignment.CENTER;

@Service
@RequiredArgsConstructor
public final class UserMovementReportBuildingService {
    private static final String DATE_TIME_PATTERN = "dd-MM-yyyy HH:mm:ss";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = ofPattern(DATE_TIME_PATTERN);

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

    private static final float PAGE_TABLE_START_X = 75F;
    private static final float PAGE_TABLE_START_Y = 650F;

    private static final float CELL_BORDER_WIDTH = 1;

    private final TrackerService trackerService;
    private final DataService dataService;

    public byte[] createReport(final User user, final DateInterval dateInterval) {
        try (final PDDocument document = new PDDocument()) {
            final Map<Tracker, List<Data>> dataGroupedByTrackers = this.findDataGroupedByAllTrackersOfUser(
                    user, dateInterval
            );
            addIntroduction(document, user, dateInterval);
            addUserTrackersTable(dataGroupedByTrackers, document);
            return transformToByteArray(document);
        } catch (final IOException cause) {
            throw new UserMovementReportBuildingException(cause);
        }
    }

    //TODO: 2 запроса объединить в один
    private Map<Tracker, List<Data>> findDataGroupedByAllTrackersOfUser(final User user,
                                                                        final DateInterval dateInterval) {
        final List<Data> data = this.dataService.findDataWithTrackerAndAddress(user, dateInterval);
        final Map<Tracker, List<Data>> dataGroupedByTrackers = groupDataByTrackersAndSortTrackersByImei(data);
        this.insertTrackersWithoutData(dataGroupedByTrackers, user);
        return dataGroupedByTrackers;
    }

    private static Map<Tracker, List<Data>> groupDataByTrackersAndSortTrackersByImei(final List<Data> data) {
        return data.stream()
                .collect(groupingBy(Data::getTracker));
    }

    private void insertTrackersWithoutData(final Map<Tracker, List<Data>> dataGroupedByTrackers, final User user) {
        final List<Tracker> userTrackers = this.trackerService.findByUser(user);
        userTrackers.forEach(
                userTracker -> dataGroupedByTrackers.computeIfAbsent(userTracker, tracker -> emptyList())
        );
    }

    private static void addIntroduction(final PDDocument document,
                                        final User user,
                                        final DateInterval dateInterval)
            throws IOException {
        final PDPage page = addPage(document);
        try (final PDPageContentStream pageContentStream = new PDPageContentStream(document, page)) {
            addIntroductionContentLines(pageContentStream, user, dateInterval);
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

    private static void addUserTrackersTable(final Map<Tracker, List<Data>> dataGroupedByTrackers,
                                             final PDDocument document) {
        final List<Table> tableParts = buildUserTrackersPaginatedTable(dataGroupedByTrackers);
        drawPaginatedUserTrackersTable(document, tableParts);
    }

    private static List<Table> buildUserTrackersPaginatedTable(final Map<Tracker, List<Data>> dataGroupedByTrackers) {
        final UserTrackersPaginatedTableBuilder builder = new UserTrackersPaginatedTableBuilder();
        dataGroupedByTrackers
                .forEach(
                        (tracker, trackerData) -> builder.addRow(
                                createUserTrackersTableRow(tracker, trackerData)
                        )
                );
        return builder.build();
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

    private static void drawPaginatedUserTrackersTable(final PDDocument document, final List<Table> paginatedTable) {
        paginatedTable.forEach(table -> drawPageTable(document, table));
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

    private static TextCell createTextCell(final int content) {
        return createTextCell(content, value -> Integer.toString(value));
    }

    private static TextCell createCell(final LocalDateTime content) {
        return createTextCell(content, DATE_TIME_FORMATTER::format);
    }

    private static TextCell createTextCell(final double content) {
        return createTextCell(content, value -> Double.toString(value));
    }

    private static TextCell createTextCell(final String content) {
        return createTextCell(content, identity());
    }

    private static <T> TextCell createTextCell(final T content, final Function<T, String> transformerContentToString) {
        final String contentAsString = transformerContentToString.apply(content);
        return TextCell.builder()
                .text(contentAsString)
                .borderWidth(CELL_BORDER_WIDTH)
                .build();
    }

    private static List<Table> buildUserMovementTable(final Map<Tracker, List<Data>> dataGroupedByTrackers) {
        dataGroupedByTrackers.entrySet().removeIf(dataByTracker -> dataByTracker.getValue().isEmpty());
        return dataGroupedByTrackers.entrySet()
                .stream()
                .map(trackerWithData -> buildUserMovementTable(trackerWithData.getKey(), trackerWithData.getValue()))
                .flatMap(Collection::stream)
                .toList();
    }

    private static List<Table> buildUserMovementTable(final Tracker userTracker, final List<Data> trackerData) {
        final UserMovementPaginatedTableBuilder tableBuilder = new UserMovementPaginatedTableBuilder(userTracker);
        trackerData.forEach(data -> tableBuilder.addRow(createUserMovementTableRow(data)));
        return tableBuilder.build();
    }

    private static Row createUserMovementTableRow(final Data data) {
        return Row.builder()
                .add(createCell(LocalDateTime.of(data.getDate(), data.getTime())))
                .add(createTextCell(data.getLatitude().findDoubleValue()))
                .add(createTextCell(data.getLongitude().findDoubleValue()))
                .add(createTextCell(data.getAddress().getCityName()))
                .add(createTextCell(data.getAddress().getCountryName()))
                .build();
    }

    private static byte[] transformToByteArray(final PDDocument document)
            throws IOException {
        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            document.save(outputStream);
            return outputStream.toByteArray();
        }
    }

    private static abstract class PaginatedTableBuilder {
        private final float[] columnsWidths;
        private final PDFont font;
        private final Integer fontSize;
        private final Color borderColor;
        private final int maxAmountOfRowsInOneTable;
        private final Row nameRow;
        private final Row headerRow;
        private final List<Table> builtTables;
        private int amountOfRowsInCurrentTable;
        private TableBuilder currentTableBuilder;

        public PaginatedTableBuilder(final float[] columnsWidths,
                                     final PDFont font,
                                     final Integer fontSize,
                                     final Color borderColor,
                                     final int maxAmountOfRowsInOneTable,
                                     final Row nameRow,
                                     final Row headerRow) {
            this.columnsWidths = columnsWidths;
            this.font = font;
            this.fontSize = fontSize;
            this.borderColor = borderColor;
            this.maxAmountOfRowsInOneTable = maxAmountOfRowsInOneTable;
            this.nameRow = nameRow;
            this.headerRow = headerRow;
            this.builtTables = new ArrayList<>();
            this.amountOfRowsInCurrentTable = 0;
            this.resetTableBuilder();
        }

        public final void addRow(final Row row) {
            if (this.amountOfRowsInCurrentTable >= this.maxAmountOfRowsInOneTable) {
                this.finishBuildingTable();
            }
            this.currentTableBuilder.addRow(row);
            this.amountOfRowsInCurrentTable++;
        }

        public final List<Table> build() {
            this.finishBuildingTable();
            return this.builtTables;
        }

        private void finishBuildingTable() {
            final Table builtTable = this.currentTableBuilder.build();
            this.builtTables.add(builtTable);
            this.resetTableBuilder();
            this.amountOfRowsInCurrentTable = 0;
        }

        private void resetTableBuilder() {
            this.currentTableBuilder = Table.builder()
                    .addColumnsOfWidth(this.columnsWidths)
                    .font(this.font)
                    .fontSize(this.fontSize)
                    .borderColor(this.borderColor)
                    .addRow(this.nameRow)
                    .addRow(this.headerRow);
        }
    }

    private static final class UserTrackersPaginatedTableBuilder extends PaginatedTableBuilder {
        private static final float TABLE_COLUMN_WIDTH_OF_IMEI = 150;
        private static final float TABLE_COLUMN_WIDTH_OF_PHONE_NUMBER = 150;
        private static final float TABLE_COLUMN_WIDTH_OF_COUNT_OF_POINTS = 150;
        private static final float[] TABLE_COLUMNS_WIDTHS = {
                TABLE_COLUMN_WIDTH_OF_IMEI,
                TABLE_COLUMN_WIDTH_OF_PHONE_NUMBER,
                TABLE_COLUMN_WIDTH_OF_COUNT_OF_POINTS
        };

        private static final PDFont TABLE_FONT = HELVETICA;
        private static final Integer TABLE_FONT_SIZE = 10;
        private static final Color TABLE_BORDER_COLOR = WHITE;
        private static final int MAX_AMOUNT_OF_ROWS_IN_ONE_TABLE = 30;

        //For row with name
        private static final Color TABLE_NAME_ROW_BACKGROUND_COLOR = BLUE;
        private static final Color TABLE_NAME_ROW_TEXT_COLOR = WHITE;
        private static final PDFont TABLE_NAME_ROW_FONT = HELVETICA_BOLD;
        private static final Integer TABLE_NAME_ROW_FONT_SIZE = 11;
        private static final HorizontalAlignment TABLE_NAME_ROW_HORIZONTAL_ALIGNMENT = CENTER;
        private static final int TABLE_NAME_ROW_COL_SPAN = 3;
        private static final String TABLE_NAME_ROW_CONTENT = "Trackers";

        //For header row
        private static final String TABLE_HEADER_COLUMN_OF_IMEI_NAME = "Imei";
        private static final String TABLE_HEADER_COLUMN_OF_PHONE_NUMBER_NAME = "Phone number";
        private static final String TABLE_HEADER_COLUMN_OF_COUNT_OF_POINTS_NAME = "Count of points";
        private static final Color TABLE_HEADER_ROW_BACKGROUND_COLOR = BLUE;
        private static final Color TABLE_HEADER_ROW_TEXT_COLOR = WHITE;
        private static final PDFont TABLE_HEADER_ROW_FONT = HELVETICA_BOLD;
        private static final Integer TABLE_HEADER_ROW_FONT_SIZE = 11;
        private static final HorizontalAlignment TABLE_HEADER_ROW_HORIZONTAL_ALIGNMENT = CENTER;

        public UserTrackersPaginatedTableBuilder() {
            super(
                    TABLE_COLUMNS_WIDTHS,
                    TABLE_FONT,
                    TABLE_FONT_SIZE,
                    TABLE_BORDER_COLOR,
                    MAX_AMOUNT_OF_ROWS_IN_ONE_TABLE,
                    buildNameRow(),
                    buildHeaderRow()
            );
        }

        private static Row buildNameRow() {
            return Row.builder()
                    .add(
                            TextCell.builder()
                                    .backgroundColor(TABLE_NAME_ROW_BACKGROUND_COLOR)
                                    .textColor(TABLE_NAME_ROW_TEXT_COLOR)
                                    .font(TABLE_NAME_ROW_FONT)
                                    .fontSize(TABLE_NAME_ROW_FONT_SIZE)
                                    .horizontalAlignment(TABLE_NAME_ROW_HORIZONTAL_ALIGNMENT)
                                    .colSpan(TABLE_NAME_ROW_COL_SPAN)
                                    .text(TABLE_NAME_ROW_CONTENT)
                                    .build()
                    )
                    .build();
        }

        private static Row buildHeaderRow() {
            return Row.builder()
                    .add(createTextCell(TABLE_HEADER_COLUMN_OF_IMEI_NAME))
                    .add(createTextCell(TABLE_HEADER_COLUMN_OF_PHONE_NUMBER_NAME))
                    .add(createTextCell(TABLE_HEADER_COLUMN_OF_COUNT_OF_POINTS_NAME))
                    .backgroundColor(TABLE_HEADER_ROW_BACKGROUND_COLOR)
                    .textColor(TABLE_HEADER_ROW_TEXT_COLOR)
                    .font(TABLE_HEADER_ROW_FONT)
                    .fontSize(TABLE_HEADER_ROW_FONT_SIZE)
                    .horizontalAlignment(TABLE_HEADER_ROW_HORIZONTAL_ALIGNMENT)
                    .build();
        }
    }

    private static final class UserMovementPaginatedTableBuilder extends PaginatedTableBuilder {
        private static final float TABLE_COLUMN_WIDTH_OF_DATETIME = 90;
        private static final float TABLE_COLUMN_WIDTH_OF_LATITUDE = 90;
        private static final float TABLE_COLUMN_WIDTH_OF_LONGITUDE = 90;
        private static final float TABLE_COLUMN_WIDTH_OF_CITY = 90;
        private static final float TABLE_COLUMN_WIDTH_OF_COUNTRY = 90;
        private static final float[] TABLE_COLUMNS_WIDTHS = {
                TABLE_COLUMN_WIDTH_OF_DATETIME,
                TABLE_COLUMN_WIDTH_OF_LATITUDE,
                TABLE_COLUMN_WIDTH_OF_LONGITUDE,
                TABLE_COLUMN_WIDTH_OF_CITY,
                TABLE_COLUMN_WIDTH_OF_COUNTRY
        };

        private static final PDFont TABLE_FONT = HELVETICA;
        private static final Integer TABLE_FONT_SIZE = 10;
        private static final Color TABLE_BORDER_COLOR = WHITE;
        private static final int MAX_AMOUNT_OF_ROWS_IN_ONE_TABLE = 30;

        //For row with name
        private static final Color TABLE_NAME_ROW_BACKGROUND_COLOR = BLUE;
        private static final Color TABLE_NAME_ROW_TEXT_COLOR = WHITE;
        private static final PDFont TABLE_NAME_ROW_FONT = HELVETICA_BOLD;
        private static final Integer TABLE_NAME_ROW_FONT_SIZE = 11;
        private static final HorizontalAlignment TABLE_NAME_ROW_HORIZONTAL_ALIGNMENT = CENTER;
        private static final int TABLE_NAME_ROW_COL_SPAN = TABLE_COLUMNS_WIDTHS.length;
        private static final String TABLE_TEMPLATE_NAME_ROW_CONTENT = "User's movement(tracker's imei '%s')";

        //For header row
        private static final String TABLE_HEADER_COLUMN_OF_DATE_TIME_NAME = "Datetime";
        private static final String TABLE_HEADER_COLUMN_OF_LATITUDE_NAME = "Latitude";
        private static final String TABLE_HEADER_COLUMN_OF_LONGITUDE_NAME = "Longitude";
        private static final String TABLE_HEADER_COLUMN_OF_CITY_NAME = "City";
        private static final String TABLE_HEADER_COLUMN_OF_COUNTRY_NAME = "Country";
        private static final Color TABLE_HEADER_ROW_BACKGROUND_COLOR = BLUE;
        private static final Color TABLE_HEADER_ROW_TEXT_COLOR = WHITE;
        private static final PDFont TABLE_HEADER_ROW_FONT = HELVETICA_BOLD;
        private static final Integer TABLE_HEADER_ROW_FONT_SIZE = 11;
        private static final HorizontalAlignment TABLE_HEADER_ROW_HORIZONTAL_ALIGNMENT = CENTER;

        public UserMovementPaginatedTableBuilder(final Tracker userTracker) {
            super(
                    TABLE_COLUMNS_WIDTHS,
                    TABLE_FONT,
                    TABLE_FONT_SIZE,
                    TABLE_BORDER_COLOR,
                    MAX_AMOUNT_OF_ROWS_IN_ONE_TABLE,
                    buildNameRow(userTracker),
                    buildHeaderRow()
            );
        }

        private static Row buildNameRow(final Tracker userTracker) {
            return Row.builder()
                    .add(
                            TextCell.builder()
                                    .backgroundColor(TABLE_NAME_ROW_BACKGROUND_COLOR)
                                    .textColor(TABLE_NAME_ROW_TEXT_COLOR)
                                    .font(TABLE_NAME_ROW_FONT)
                                    .fontSize(TABLE_NAME_ROW_FONT_SIZE)
                                    .horizontalAlignment(TABLE_NAME_ROW_HORIZONTAL_ALIGNMENT)
                                    .colSpan(TABLE_NAME_ROW_COL_SPAN)
                                    .text(createTableName(userTracker))
                                    .build()
                    )
                    .build();
        }

        private static String createTableName(final Tracker userTracker) {
            final String trackerImei = userTracker.getImei();
            return format(TABLE_TEMPLATE_NAME_ROW_CONTENT, trackerImei);
        }

        private static Row buildHeaderRow() {
            return Row.builder()
                    .add(createTextCell(TABLE_HEADER_COLUMN_OF_DATE_TIME_NAME))
                    .add(createTextCell(TABLE_HEADER_COLUMN_OF_LATITUDE_NAME))
                    .add(createTextCell(TABLE_HEADER_COLUMN_OF_LONGITUDE_NAME))
                    .add(createTextCell(TABLE_HEADER_COLUMN_OF_CITY_NAME))
                    .add(createTextCell(TABLE_HEADER_COLUMN_OF_COUNTRY_NAME))
                    .backgroundColor(TABLE_HEADER_ROW_BACKGROUND_COLOR)
                    .textColor(TABLE_HEADER_ROW_TEXT_COLOR)
                    .font(TABLE_HEADER_ROW_FONT)
                    .fontSize(TABLE_HEADER_ROW_FONT_SIZE)
                    .horizontalAlignment(TABLE_HEADER_ROW_HORIZONTAL_ALIGNMENT)
                    .build();
        }
    }
}
