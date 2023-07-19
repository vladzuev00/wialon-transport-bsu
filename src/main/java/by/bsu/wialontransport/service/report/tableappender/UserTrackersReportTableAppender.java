package by.bsu.wialontransport.service.report.tableappender;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.service.report.model.TableRowMetaData;
import by.bsu.wialontransport.service.report.model.UserMovementReportBuildingContext;
import by.bsu.wialontransport.service.report.tabledrawer.DistributedReportTableDrawer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.cell.AbstractCell;

import java.awt.*;
import java.util.List;
import java.util.Map.Entry;

import static by.bsu.wialontransport.util.PDFCellFactoryUtil.createTextCell;
import static java.awt.Color.*;
import static org.vandeseer.easytable.settings.HorizontalAlignment.CENTER;

@Order(1)
@Component
public final class UserTrackersReportTableAppender extends AbstractReportTableAppender {
    private static final float TABLE_COLUMN_WIDTH_OF_IMEI = 200;
    private static final float TABLE_COLUMN_WIDTH_OF_PHONE_NUMBER = 200;
    private static final float TABLE_COLUMN_WIDTH_OF_COUNT_OF_POINTS = 200;
    private static final float[] TABLE_COLUMNS_WIDTHS = {
            TABLE_COLUMN_WIDTH_OF_IMEI,
            TABLE_COLUMN_WIDTH_OF_PHONE_NUMBER,
            TABLE_COLUMN_WIDTH_OF_COUNT_OF_POINTS
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
    private static final String HEADER_ROW_COLUMN_OF_IMEI_NAME = "Imei";
    private static final String HEADER_ROW_COLUMN_OF_PHONE_NUMBER_NAME = "Phone number";
    private static final String HEADER_ROW_COLUMN_OF_COUNT_OF_POINTS_NAME = "Count of points";

    private static final String TABLE_NAME = "Trackers";

    public UserTrackersReportTableAppender(final DistributedReportTableDrawer tableDrawer) {
        super(tableDrawer, TABLE_COLUMNS_WIDTHS, createNameRowMetaData(), createHeaderRowMetaData(), TABLE_NAME);
    }

    @Override
    protected List<Row> createContentRows(UserMovementReportBuildingContext context) {
        return context.getPointCountsByAllTrackers()
                .entrySet()
                .stream()
                .map(UserTrackersReportTableAppender::createUserTrackersTableRow)
                .toList();
    }

    @Override
    protected AbstractCell[] createHeaderRowCells() {
        return new AbstractCell[]{
                createTextCell(HEADER_ROW_COLUMN_OF_IMEI_NAME),
                createTextCell(HEADER_ROW_COLUMN_OF_PHONE_NUMBER_NAME),
                createTextCell(HEADER_ROW_COLUMN_OF_COUNT_OF_POINTS_NAME)
        };
    }

    private static Row createUserTrackersTableRow(final Entry<Tracker, Integer> pointCountsByTracker) {
        final Tracker tracker = pointCountsByTracker.getKey();
        final String trackerImei = tracker.getImei();
        final String trackerPhoneNumber = tracker.getPhoneNumber();
        final int countOfPoints = pointCountsByTracker.getValue();
        return Row.builder()
                .add(createTextCell(trackerImei))
                .add(createTextCell(trackerPhoneNumber))
                .add(createTextCell(countOfPoints))
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
