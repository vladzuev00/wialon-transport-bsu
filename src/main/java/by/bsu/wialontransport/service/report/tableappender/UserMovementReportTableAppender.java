package by.bsu.wialontransport.service.report.tableappender;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.service.report.model.TableRowMetaData;
import by.bsu.wialontransport.service.report.model.UserMovementReportBuildingContext;
import by.bsu.wialontransport.service.report.tabledrawer.DistributedReportTableDrawer;
import org.springframework.stereotype.Service;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.cell.AbstractCell;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Stream;

import static by.bsu.wialontransport.util.CellFactoryUtil.createTextCell;
import static java.awt.Color.BLUE;
import static java.awt.Color.WHITE;
import static org.vandeseer.easytable.settings.HorizontalAlignment.CENTER;

@Service
public final class UserMovementReportTableAppender extends AbstractReportTableAppender {
    private static final float TABLE_COLUMN_WIDTH_OF_TRACKER_IMEI = 100;
    private static final float TABLE_COLUMN_WIDTH_OF_DATETIME = 100;
    private static final float TABLE_COLUMN_WIDTH_OF_LATITUDE = 100;
    private static final float TABLE_COLUMN_WIDTH_OF_LONGITUDE = 100;
    private static final float TABLE_COLUMN_WIDTH_OF_CITY = 100;
    private static final float TABLE_COLUMN_WIDTH_OF_COUNTRY = 100;
    private static final float[] TABLE_COLUMNS_WIDTHS = {
            TABLE_COLUMN_WIDTH_OF_TRACKER_IMEI,
            TABLE_COLUMN_WIDTH_OF_DATETIME,
            TABLE_COLUMN_WIDTH_OF_LATITUDE,
            TABLE_COLUMN_WIDTH_OF_LONGITUDE,
            TABLE_COLUMN_WIDTH_OF_CITY,
            TABLE_COLUMN_WIDTH_OF_COUNTRY
    };

    //for row with table's name
    private static final Color NAME_ROW_BACKGROUND_COLOR = BLUE;
    private static final Color NAME_ROW_TEXT_COLOR = WHITE;
    private static final Integer NAME_ROW_FONT_SIZE = 11;
    private static final HorizontalAlignment NAME_ROW_HORIZONTAL_ALIGNMENT = CENTER;
    private static final float NAME_ROW_MIN_HEIGHT = 20;

    //for header row
    private static final Color HEADER_ROW_BACKGROUND_COLOR = BLUE;
    private static final Color HEADER_ROW_TEXT_COLOR = WHITE;
    private static final Integer HEADER_ROW_FONT_SIZE = 11;
    private static final HorizontalAlignment HEADER_ROW_HORIZONTAL_ALIGNMENT = CENTER;
    private static final float HEADER_ROW_MIN_HEIGHT = 20;
    private static final String TABLE_HEADER_COLUMN_OF_TRACKER_IMEI = "Tracker's imei";
    private static final String TABLE_HEADER_COLUMN_OF_DATE_TIME_NAME = "Datetime";
    private static final String TABLE_HEADER_COLUMN_OF_LATITUDE_NAME = "Latitude";
    private static final String TABLE_HEADER_COLUMN_OF_LONGITUDE_NAME = "Longitude";
    private static final String TABLE_HEADER_COLUMN_OF_CITY_NAME = "City";
    private static final String TABLE_HEADER_COLUMN_OF_COUNTRY_NAME = "Country";

    private static final String TABLE_NAME = "Movement";

    public UserMovementReportTableAppender(final DistributedReportTableDrawer tableDrawer) {
        super(tableDrawer, TABLE_COLUMNS_WIDTHS, createNameRowMetaData(), createHeaderRowMetaData(), TABLE_NAME);
    }

    @Override
    protected List<Row> createContentRows(final UserMovementReportBuildingContext context) {
        return context.getDataGroupedBySortedByImeiTrackers()
                .entrySet()
                .stream()
                .flatMap(UserMovementReportTableAppender::createTrackerMovementStreamRows)
                .toList();
    }

    @Override
    protected AbstractCell[] createHeaderRowCells() {
        return new AbstractCell[]{
                createTextCell(TABLE_HEADER_COLUMN_OF_TRACKER_IMEI),
                createTextCell(TABLE_HEADER_COLUMN_OF_DATE_TIME_NAME),
                createTextCell(TABLE_HEADER_COLUMN_OF_LATITUDE_NAME),
                createTextCell(TABLE_HEADER_COLUMN_OF_LONGITUDE_NAME),
                createTextCell(TABLE_HEADER_COLUMN_OF_CITY_NAME),
                createTextCell(TABLE_HEADER_COLUMN_OF_COUNTRY_NAME)
        };
    }

    private static Stream<Row> createTrackerMovementStreamRows(final Entry<Tracker, List<Data>> dataByTracker) {
        final Tracker tracker = dataByTracker.getKey();
        return dataByTracker.getValue()
                .stream()
                .map(data -> createTrackerMovementTableRow(tracker, data));
    }

    private static Row createTrackerMovementTableRow(final Tracker tracker, final Data data) {
        final String trackerImei = tracker.getImei();
        final LocalDateTime dateTime = data.findDateTime();
        final double latitudeAsDouble = data.findLatitudeAsDouble();
        final double longitudeAsDouble = data.findLongitudeAsDouble();
        final String cityName = data.findCityName();
        final String countryName = data.findCountryName();
        return Row.builder()
                .add(createTextCell(trackerImei))
                .add(createTextCell(dateTime))
                .add(createTextCell(latitudeAsDouble))
                .add(createTextCell(longitudeAsDouble))
                .add(createTextCell(cityName))
                .add(createTextCell(countryName))
                .build();
    }

    private static TableRowMetaData createNameRowMetaData() {
        return TableRowMetaData.builder()
                .backgroundColor(NAME_ROW_BACKGROUND_COLOR)
                .textColor(NAME_ROW_TEXT_COLOR)
                .fontSize(NAME_ROW_FONT_SIZE)
                .horizontalAlignment(NAME_ROW_HORIZONTAL_ALIGNMENT)
                .minHeight(NAME_ROW_MIN_HEIGHT)
                .build();
    }

    private static TableRowMetaData createHeaderRowMetaData() {
        return TableRowMetaData.builder()
                .backgroundColor(HEADER_ROW_BACKGROUND_COLOR)
                .textColor(HEADER_ROW_TEXT_COLOR)
                .fontSize(HEADER_ROW_FONT_SIZE)
                .horizontalAlignment(HEADER_ROW_HORIZONTAL_ALIGNMENT)
                .minHeight(HEADER_ROW_MIN_HEIGHT)
                .build();
    }
}
