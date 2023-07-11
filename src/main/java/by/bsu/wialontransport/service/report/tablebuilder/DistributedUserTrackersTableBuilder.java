package by.bsu.wialontransport.service.report.tablebuilder;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.cell.TextCell;

import java.awt.*;

import static by.bsu.wialontransport.util.CellFactoryUtil.createTextCell;
import static java.awt.Color.BLUE;
import static java.awt.Color.WHITE;
import static org.vandeseer.easytable.settings.HorizontalAlignment.CENTER;

public final class DistributedUserTrackersTableBuilder extends DistributedTableBuilder {
    private static final float TABLE_COLUMN_WIDTH_OF_IMEI = 150;
    private static final float TABLE_COLUMN_WIDTH_OF_PHONE_NUMBER = 150;
    private static final float TABLE_COLUMN_WIDTH_OF_COUNT_OF_POINTS = 150;
    private static final float[] TABLE_COLUMNS_WIDTHS = {
            TABLE_COLUMN_WIDTH_OF_IMEI,
            TABLE_COLUMN_WIDTH_OF_PHONE_NUMBER,
            TABLE_COLUMN_WIDTH_OF_COUNT_OF_POINTS
    };

    private static final Integer TABLE_FONT_SIZE = 10;
    private static final Color TABLE_BORDER_COLOR = WHITE;
    private static final int MAX_AMOUNT_OF_ROWS_IN_ONE_TABLE = 30;

    //For row with name
    private static final Color TABLE_NAME_ROW_BACKGROUND_COLOR = BLUE;
    private static final Color TABLE_NAME_ROW_TEXT_COLOR = WHITE;
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
    private static final Integer TABLE_HEADER_ROW_FONT_SIZE = 11;
    private static final HorizontalAlignment TABLE_HEADER_ROW_HORIZONTAL_ALIGNMENT = CENTER;

    public DistributedUserTrackersTableBuilder(final PDFont font) {
        super(
                TABLE_COLUMNS_WIDTHS,
                font,
                TABLE_FONT_SIZE,
                TABLE_BORDER_COLOR,
                MAX_AMOUNT_OF_ROWS_IN_ONE_TABLE,
                buildNameRow(font),
                buildHeaderRow(font)
        );
    }

    private static Row buildNameRow(final PDFont font) {
        return Row.builder()
                .add(
                        TextCell.builder()
                                .backgroundColor(TABLE_NAME_ROW_BACKGROUND_COLOR)
                                .textColor(TABLE_NAME_ROW_TEXT_COLOR)
                                .font(font)
                                .fontSize(TABLE_NAME_ROW_FONT_SIZE)
                                .horizontalAlignment(TABLE_NAME_ROW_HORIZONTAL_ALIGNMENT)
                                .colSpan(TABLE_NAME_ROW_COL_SPAN)
                                .text(TABLE_NAME_ROW_CONTENT)
                                .build()
                )
                .build();
    }

    private static Row buildHeaderRow(final PDFont font) {
        return Row.builder()
                .add(createTextCell(TABLE_HEADER_COLUMN_OF_IMEI_NAME))
                .add(createTextCell(TABLE_HEADER_COLUMN_OF_PHONE_NUMBER_NAME))
                .add(createTextCell(TABLE_HEADER_COLUMN_OF_COUNT_OF_POINTS_NAME))
                .backgroundColor(TABLE_HEADER_ROW_BACKGROUND_COLOR)
                .textColor(TABLE_HEADER_ROW_TEXT_COLOR)
                .font(font)
                .fontSize(TABLE_HEADER_ROW_FONT_SIZE)
                .horizontalAlignment(TABLE_HEADER_ROW_HORIZONTAL_ALIGNMENT)
                .build();
    }
}
