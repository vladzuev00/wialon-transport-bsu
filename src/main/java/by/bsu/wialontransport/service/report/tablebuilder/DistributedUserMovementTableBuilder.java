package by.bsu.wialontransport.service.report.tablebuilder;

import by.bsu.wialontransport.crud.dto.Tracker;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.cell.TextCell;

import java.awt.*;

import static by.bsu.wialontransport.util.CellFactoryUtil.createTextCell;
import static java.awt.Color.BLUE;
import static java.awt.Color.WHITE;
import static java.lang.String.format;
import static org.vandeseer.easytable.settings.HorizontalAlignment.CENTER;

public final class DistributedUserMovementTableBuilder extends DistributedTableBuilder {
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

    private static final Integer TABLE_FONT_SIZE = 10;
    private static final Color TABLE_BORDER_COLOR = WHITE;
    private static final int MAX_AMOUNT_OF_ROWS_IN_ONE_TABLE = 30;

    //For row with name
    private static final Color TABLE_NAME_ROW_BACKGROUND_COLOR = BLUE;
    private static final Color TABLE_NAME_ROW_TEXT_COLOR = WHITE;
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
    private static final Integer TABLE_HEADER_ROW_FONT_SIZE = 11;
    private static final HorizontalAlignment TABLE_HEADER_ROW_HORIZONTAL_ALIGNMENT = CENTER;

    public DistributedUserMovementTableBuilder(final PDFont font, final Tracker userTracker) {
        super(
                TABLE_COLUMNS_WIDTHS,
                font,
                TABLE_FONT_SIZE,
                TABLE_BORDER_COLOR,
                MAX_AMOUNT_OF_ROWS_IN_ONE_TABLE,
                buildNameRow(font, userTracker),
                buildHeaderRow(font)
        );
    }

    //TODO: refactor
    private static Row buildNameRow(final PDFont font, final Tracker userTracker) {
        return Row.builder()
                .add(
                        TextCell.builder()
                                .backgroundColor(TABLE_NAME_ROW_BACKGROUND_COLOR)
                                .textColor(TABLE_NAME_ROW_TEXT_COLOR)
                                .font(font)
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

    //TODO: refactor
    private static Row buildHeaderRow(final PDFont font) {
        return Row.builder()
                .add(createTextCell(TABLE_HEADER_COLUMN_OF_DATE_TIME_NAME))
                .add(createTextCell(TABLE_HEADER_COLUMN_OF_LATITUDE_NAME))
                .add(createTextCell(TABLE_HEADER_COLUMN_OF_LONGITUDE_NAME))
                .add(createTextCell(TABLE_HEADER_COLUMN_OF_CITY_NAME))
                .add(createTextCell(TABLE_HEADER_COLUMN_OF_COUNTRY_NAME))
                .backgroundColor(TABLE_HEADER_ROW_BACKGROUND_COLOR)
                .textColor(TABLE_HEADER_ROW_TEXT_COLOR)
                .font(font)
                .fontSize(TABLE_HEADER_ROW_FONT_SIZE)
                .horizontalAlignment(TABLE_HEADER_ROW_HORIZONTAL_ALIGNMENT)
                .build();
    }
}
