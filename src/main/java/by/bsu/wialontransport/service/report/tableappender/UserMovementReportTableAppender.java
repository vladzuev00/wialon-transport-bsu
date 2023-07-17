package by.bsu.wialontransport.service.report.tableappender;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.service.report.model.metadata.TableNameRowMetaData;
import by.bsu.wialontransport.service.report.model.UserMovementReportBuildingContext;
import by.bsu.wialontransport.service.report.tabledrawer.DistributedReportTableDrawer;
import org.springframework.stereotype.Service;
import org.vandeseer.easytable.settings.HorizontalAlignment;
import org.vandeseer.easytable.structure.Row;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

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

    private static final Color NAME_ROW_BACKGROUND_COLOR = BLUE;
    private static final Color NAME_ROW_TEXT_COLOR = WHITE;
    private static final Integer NAME_ROW_FONT_SIZE = 11;
    private static final HorizontalAlignment NAME_ROW_HORIZONTAL_ALIGNMENT = CENTER;
    private static final float NAME_ROW_MIN_HEIGHT = 20;
    private static final String NAME_ROW_CONTENT = "Movement";

    public UserMovementReportTableAppender(final DistributedReportTableDrawer tableDrawer) {
        super(tableDrawer, TABLE_COLUMNS_WIDTHS, createNameRowMetaData());
    }

    @Override
    protected List<Row> createContentRows(final UserMovementReportBuildingContext context) {

        return null;
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

    private static TableNameRowMetaData createNameRowMetaData() {
        return TableNameRowMetaData.builder()
                .backgroundColor(NAME_ROW_BACKGROUND_COLOR)
                .textColor(NAME_ROW_TEXT_COLOR)
                .fontSize(NAME_ROW_FONT_SIZE)
                .horizontalAlignment(NAME_ROW_HORIZONTAL_ALIGNMENT)
                .minHeight(NAME_ROW_MIN_HEIGHT)
                .tableName(NAME_ROW_CONTENT)
                .build();
    }
}
