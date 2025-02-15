package by.vladzuev.locationreceiver.service.report.tableappender;

import by.vladzuev.locationreceiver.crud.dto.Tracker;
import by.vladzuev.locationreceiver.model.Mileage;
import by.vladzuev.locationreceiver.service.report.model.TableRowMetaData;
import by.vladzuev.locationreceiver.service.report.model.TrackerMovement;
import by.vladzuev.locationreceiver.service.report.tabledrawer.DistributedReportTableDrawer;
import by.vladzuev.locationreceiver.util.PDFCellFactoryUtil;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.cell.AbstractCell;

import java.awt.*;
import java.util.stream.Stream;

import static by.vladzuev.locationreceiver.util.PDFCellFactoryUtil.createTextCell;
import static java.awt.Color.BLUE;
import static java.awt.Color.WHITE;
import static org.vandeseer.easytable.settings.HorizontalAlignment.CENTER;

@Order(2)
@Component
public final class UserMileageReportTableAppender extends AbstractUserMovementReportTableAppender {
    private static final float TABLE_COLUMN_WIDTH_OF_TRACKER_IMEI = 150;
    private static final float TABLE_COLUMN_WIDTH_OF_URBAN_MILEAGE = 150;
    private static final float TABLE_COLUMN_WIDTH_OF_COUNTRY_MILEAGE = 150;
    private static final float TABLE_COLUMN_WIDTH_OF_TOTAL_MILEAGE = 150;
    private static final float[] TABLE_COLUMNS_WIDTHS = {
            TABLE_COLUMN_WIDTH_OF_TRACKER_IMEI,
            TABLE_COLUMN_WIDTH_OF_URBAN_MILEAGE,
            TABLE_COLUMN_WIDTH_OF_COUNTRY_MILEAGE,
            TABLE_COLUMN_WIDTH_OF_TOTAL_MILEAGE
    };

    //for row with table's name
    private static final Color NAME_ROW_BACKGROUND_COLOR = BLUE;
    private static final Color NAME_ROW_TEXT_COLOR = WHITE;
    private static final Integer NAME_ROW_FONT_SIZE = 13;
    private static final HorizontalAlignment NAME_ROW_HORIZONTAL_ALIGNMENT = CENTER;
    private static final float NAME_ROW_MIN_HEIGHT = 20;

    //for header row
    private static final Color HEADER_ROW_BACKGROUND_COLOR = BLUE;
    private static final Color HEADER_ROW_TEXT_COLOR = WHITE;
    private static final Integer HEADER_ROW_FONT_SIZE = 11;
    private static final HorizontalAlignment HEADER_ROW_HORIZONTAL_ALIGNMENT = CENTER;
    private static final float HEADER_ROW_MIN_HEIGHT = 20;
    private static final String HEADER_ROW_COLUMN_OF_TRACKER_IMEI_NAME = "Tracker's imei";
    private static final String HEADER_ROW_COLUMN_OF_URBAN_MILEAGE_NAME = "Urban mileage";
    private static final String HEADER_ROW_COLUMN_OF_COUNTRY_MILEAGE_NAME = "Country's mileage";
    private static final String HEADER_ROW_COLUMN_OF_TOTAL_MILEAGE_NAME = "Total mileage";

    private static final String TABLE_NAME = "Mileage";

    public UserMileageReportTableAppender(final DistributedReportTableDrawer tableDrawer) {
        super(tableDrawer, TABLE_COLUMNS_WIDTHS, createNameRowMetaData(), createHeaderRowMetaData(), TABLE_NAME);
    }

    @Override
    protected Stream<Row> createContentRowStream(final TrackerMovement movement) {
        final Row contentRow = createContentRow(movement);
        return Stream.of(contentRow);
    }

    @Override
    protected AbstractCell[] createHeaderRowCells() {
        return new AbstractCell[]{
                PDFCellFactoryUtil.createTextCell(HEADER_ROW_COLUMN_OF_TRACKER_IMEI_NAME),
                PDFCellFactoryUtil.createTextCell(HEADER_ROW_COLUMN_OF_URBAN_MILEAGE_NAME),
                PDFCellFactoryUtil.createTextCell(HEADER_ROW_COLUMN_OF_COUNTRY_MILEAGE_NAME),
                PDFCellFactoryUtil.createTextCell(HEADER_ROW_COLUMN_OF_TOTAL_MILEAGE_NAME)
        };
    }

    private static Row createContentRow(final TrackerMovement movement) {
        final Tracker tracker = movement.getTracker();
        final String trackerImei = tracker.getImei();
        final Mileage mileage = movement.getMileage();
        final double urbanMileage = mileage.getUrban();
        final double countryMileage = mileage.getCountry();
        final double totalMileage = urbanMileage + countryMileage;
        return Row.builder()
                .add(PDFCellFactoryUtil.createTextCell(trackerImei))
                .add(PDFCellFactoryUtil.createTextCell(urbanMileage))
                .add(PDFCellFactoryUtil.createTextCell(countryMileage))
                .add(PDFCellFactoryUtil.createTextCell(totalMileage))
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
