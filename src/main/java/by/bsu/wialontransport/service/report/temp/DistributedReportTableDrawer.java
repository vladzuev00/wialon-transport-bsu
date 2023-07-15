package by.bsu.wialontransport.service.report.temp;

import by.bsu.wialontransport.service.report.model.ReportTable;
import by.bsu.wialontransport.service.report.tabledrawer.exception.DistributedTableDrawingException;
import lombok.Value;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.springframework.stereotype.Component;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.Table.TableBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

import static by.bsu.wialontransport.util.PDFUtil.addPage;
import static java.lang.Float.compare;

@Component
public final class DistributedReportTableDrawer {
    private static final float PAGE_TABLE_START_Y = 750F;
    private static final float PAGE_TABLE_END_Y = 50F;

    public void draw(final ReportTable table, final PDDocument document) {
        final List<Table> pageTables = mapToPageTables(table);
        final float tableWidth = findWidth(table);
        drawPageTables(pageTables, document, tableWidth);
    }

    private static List<Table> mapToPageTables(final ReportTable table) {
        final Supplier<TableBuilder> tableBuilderFactory = () -> createTableBuilder(table);
        return distributeRowsAmongPages(table.getRows())
                .stream()
                .map(pageTableRows -> createTablePage(pageTableRows, tableBuilderFactory))
                .toList();
    }

    private static TableBuilder createTableBuilder(final ReportTable table) {
        return Table.builder()
                .addColumnsOfWidth(table.getColumnWidths())
                .font(table.getFont())
                .fontSize(table.getFontSize())
                .borderColor(table.getBorderColor());
    }

    /**
     * @param rows - table's rows
     * @return list of distributed rows among pages. List's element - list of rows of page's table
     */
    //TODO: refactor
    private static List<List<Row>> distributeRowsAmongPages(final List<Row> rows) {
        final List<List<Row>> distributedRows = new ArrayList<>();
        final Iterator<Row> rowIterator = rows.iterator();
        List<Row> currentPageRows = new ArrayList<>();
        float valueIteratingByPageY = PAGE_TABLE_START_Y;
        while (rowIterator.hasNext()) {
            final Row currentRow = rowIterator.next();
            valueIteratingByPageY -= currentRow.getHeight();
            if (compare(valueIteratingByPageY, PAGE_TABLE_END_Y) > 0) {
                currentPageRows.add(currentRow);
            } else {
                distributedRows.add(currentPageRows);
                currentPageRows = new ArrayList<>();
                valueIteratingByPageY = PAGE_TABLE_START_Y;
            }
        }
        distributedRows.add(currentPageRows);
        return distributedRows;
    }

    private static Table createTablePage(final List<Row> rows, final Supplier<TableBuilder> tableBuilderFactory) {
        final TableBuilder tableBuilder = tableBuilderFactory.get();
        rows.forEach(tableBuilder::addRow);
        return tableBuilder.build();
    }

    private static float findWidth(final ReportTable table) {
        float result = 0;
        for (final float columnWidth : table.getColumnWidths()) {
            result += columnWidth;
        }
        return result;
    }

    private static void drawPageTables(final List<Table> pageTables, final PDDocument document, final float tableWidth) {
        pageTables.forEach(pageTable -> drawPageTable(pageTable, document, tableWidth));
    }

    private static void drawPageTable(final Table pageTable, final PDDocument document, final float tableWidth) {
        final PDPage page = addPage(document);
        final PDFPageCoordinate pageTableStartCoordinate = findPageTableStartCoordinate(page, tableWidth);
        try (final PDPageContentStream pageContentStream = new PDPageContentStream(document, page)) {
            drawPageTable(pageContentStream, pageTable, pageTableStartCoordinate);
        } catch (final IOException cause) {
            throw new DistributedTableDrawingException(cause);
        }
    }

    private static PDFPageCoordinate findPageTableStartCoordinate(final PDPage page, final float tableWidth) {
        final float pageWidth = findPageWidth(page);
        final float pageTableStartX = (pageWidth - tableWidth) / 2;
        return new PDFPageCoordinate(pageTableStartX, PAGE_TABLE_START_Y);
    }

    private static float findPageWidth(final PDPage page) {
        final PDRectangle pageBoundingBox = page.getBBox();
        return pageBoundingBox.getWidth();
    }

    private static void drawPageTable(final PDPageContentStream pageContentStream,
                                      final Table pageTable,
                                      final PDFPageCoordinate pageTableStartCoordinate) {
        TableDrawer.builder()
                .contentStream(pageContentStream)
                .table(pageTable)
                .startX(pageTableStartCoordinate.getX())
                .startY(pageTableStartCoordinate.getY())
                .build()
                .draw();
    }


    @Value
    private static class PDFPageCoordinate {
        float x;
        float y;
    }

}
