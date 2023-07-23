package by.bsu.wialontransport.service.report.tableappender;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.model.Mileage;
import by.bsu.wialontransport.service.report.model.TableRowMetaData;
import by.bsu.wialontransport.service.report.model.TrackerMovement;
import by.bsu.wialontransport.service.report.model.UserMovementReportBuildingContext;
import by.bsu.wialontransport.service.report.tabledrawer.DistributedReportTableDrawer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.cell.AbstractCell;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static by.bsu.wialontransport.util.PDFCellFactoryUtil.createTextCell;
import static by.bsu.wialontransport.util.collection.CollectionUtil.collectToTreeMap;
import static java.awt.Color.BLUE;
import static java.awt.Color.WHITE;
import static java.util.Comparator.comparing;
import static org.vandeseer.easytable.settings.HorizontalAlignment.CENTER;

@Order(2)
@Component
public final class UserMileageTableAppender extends AbstractReportTableAppender {
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

    public UserMileageTableAppender(final DistributedReportTableDrawer tableDrawer) {
        super(tableDrawer, TABLE_COLUMNS_WIDTHS, createNameRowMetaData(), createHeaderRowMetaData(), TABLE_NAME);
    }

    @Override
    protected List<Row> createContentRows(final UserMovementReportBuildingContext context) {
        return findMileagesByTrackersSortedByImei(context)
                .entrySet()
                .stream()
                .map(UserMileageTableAppender::createContentRow)
                .toList();
    }

    @Override
    protected AbstractCell[] createHeaderRowCells() {
        return new AbstractCell[]{
                createTextCell(HEADER_ROW_COLUMN_OF_TRACKER_IMEI_NAME),
                createTextCell(HEADER_ROW_COLUMN_OF_URBAN_MILEAGE_NAME),
                createTextCell(HEADER_ROW_COLUMN_OF_COUNTRY_MILEAGE_NAME),
                createTextCell(HEADER_ROW_COLUMN_OF_TOTAL_MILEAGE_NAME)
        };
    }

    private static Map<Tracker, Mileage> findMileagesByTrackersSortedByImei(final UserMovementReportBuildingContext context) {
        return collectToTreeMap(
                context.getTrackerMovements(),
                TrackerMovement::getTracker,
                TrackerMovement::getMileage,
                comparing(Tracker::getImei)
        );
    }

    private static Row createContentRow(final Entry<Tracker, Mileage> mileageByTracker) {
        final Tracker tracker = mileageByTracker.getKey();
        final String trackerImei = tracker.getImei();
        final Mileage mileage = mileageByTracker.getValue();
        final double urbanMileage = mileage.getUrban();
        final double countryMileage = mileage.getCountry();
        final double totalMileage = urbanMileage + countryMileage;
        return Row.builder()
                .add(createTextCell(trackerImei))
                .add(createTextCell(urbanMileage))
                .add(createTextCell(countryMileage))
                .add(createTextCell(totalMileage))
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
