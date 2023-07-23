package by.bsu.wialontransport.service.report.tableappender;

import by.bsu.wialontransport.service.report.model.ReportTable;
import by.bsu.wialontransport.service.report.model.TableRowMetaData;
import by.bsu.wialontransport.service.report.model.TrackerMovement;
import by.bsu.wialontransport.service.report.model.UserMovementReportBuildingContext;
import by.bsu.wialontransport.service.report.tabledrawer.DistributedReportTableDrawer;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Row.RowBuilder;
import org.vandeseer.easytable.structure.cell.AbstractCell;
import org.vandeseer.easytable.structure.cell.TextCell;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
public abstract class AbstractReportTableAppender {
    private final DistributedReportTableDrawer tableDrawer;
    private final float[] columnWidths;
    private final TableRowMetaData nameRowMetaData;
    private final TableRowMetaData headerRowMetaData;
    private final String tableName;

    public final void appendTableToContextDocument(final UserMovementReportBuildingContext context) {
        final ReportTable table = this.buildTable(context);
        this.tableDrawer.draw(table, context.getDocument());
    }

    protected abstract Stream<Row> createContentRowStream(final TrackerMovement movement);

    protected abstract AbstractCell[] createHeaderRowCells();

    private ReportTable buildTable(final UserMovementReportBuildingContext context) {
        return ReportTable.builder()
                .rows(this.createTableRows(context))
                .columnWidths(this.columnWidths)
                .font(context.getFont())
                .fontSize(context.getFontSize())
                .borderColor(context.getBorderColor())
                .build();
    }

    private List<Row> createTableRows(final UserMovementReportBuildingContext context) {
        final PDFont font = context.getFont();
        final Row nameRow = this.createNameRow(font);
        final Row headerRow = this.createHeaderRow(font);
        final List<Row> contentRows = this.createContentRows(context);
        final List<Row> tableRows = new ArrayList<>();
        tableRows.add(nameRow);
        tableRows.add(headerRow);
        tableRows.addAll(contentRows);
        return tableRows;
    }

    private Row createNameRow(final PDFont font) {
        return Row.builder()
                .add(
                        TextCell.builder()
                                .backgroundColor(this.nameRowMetaData.getBackgroundColor())
                                .textColor(this.nameRowMetaData.getTextColor())
                                .font(font)
                                .fontSize(this.nameRowMetaData.getFontSize())
                                .horizontalAlignment(this.nameRowMetaData.getHorizontalAlignment())
                                .colSpan(this.columnWidths.length)
                                .minHeight(this.nameRowMetaData.getMinHeight())
                                .text(this.tableName)
                                .build()
                )
                .build();
    }

    private Row createHeaderRow(final PDFont font) {
        final RowBuilder builder = Row.builder()
                .backgroundColor(this.headerRowMetaData.getBackgroundColor())
                .textColor(this.headerRowMetaData.getTextColor())
                .font(font)
                .fontSize(this.headerRowMetaData.getFontSize())
                .horizontalAlignment(this.headerRowMetaData.getHorizontalAlignment());
        this.addHeaderRowCells(builder);
        return builder.build();
    }

    private void addHeaderRowCells(final RowBuilder builder) {
        final AbstractCell[] cells = this.createHeaderRowCells();
        for (final AbstractCell cell : cells) {
            builder.add(cell);
        }
    }

    private List<Row> createContentRows(final UserMovementReportBuildingContext context) {
        return context.getTrackerMovements()
                .stream()
                .flatMap(this::createContentRowStream)
                .toList();
    }
}
