package by.bsu.wialontransport.service.report.tablebuilder;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.TextCell;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.function.Function.identity;

public abstract class DistributedTableBuilder {
    private static final float CELL_BORDER_WIDTH = 1;

    private static final String CELL_DATE_TIME_PATTERN = "dd-MM-yyyy HH:mm:ss";
    private static final DateTimeFormatter CELL_DATE_TIME_FORMATTER = ofPattern(CELL_DATE_TIME_PATTERN);

    private final float[] columnsWidths;
    private final PDFont font;
    private final Integer fontSize;
    private final Color borderColor;
    private final int maxAmountOfRowsInOnePage;
    private final Row nameRow;
    private final Row headerRow;
    private final java.util.List<Table> pageTables;
    private int amountOfRowsInCurrentTable;
    private Table.TableBuilder currentTableBuilder;

    public DistributedTableBuilder(final float[] columnsWidths,
                                   final PDFont font,
                                   final Integer fontSize,
                                   final Color borderColor,
                                   final int maxAmountOfRowsInOnePage,
                                   final Row nameRow,
                                   final Row headerRow) {
        this.columnsWidths = columnsWidths;
        this.font = font;
        this.fontSize = fontSize;
        this.borderColor = borderColor;
        this.maxAmountOfRowsInOnePage = maxAmountOfRowsInOnePage;
        this.nameRow = nameRow;
        this.headerRow = headerRow;
        this.pageTables = new ArrayList<>();
        this.amountOfRowsInCurrentTable = 0;
        this.resetTableBuilder();
    }

    public final void addRow(final Row row) {
        if (this.amountOfRowsInCurrentTable >= this.maxAmountOfRowsInOnePage) {
            this.finishBuildingPageTable();
        }
        this.currentTableBuilder.addRow(row);
        this.amountOfRowsInCurrentTable++;
    }

    public final List<Table> build() {
        this.finishBuildingPageTable();
        return this.pageTables;
    }

    protected static TextCell createTextCell(final int content) {
        return createTextCell(content, value -> Integer.toString(value));
    }

    protected static TextCell createCell(final LocalDateTime content) {
        return createTextCell(content, CELL_DATE_TIME_FORMATTER::format);
    }

    protected static TextCell createTextCell(final double content) {
        return createTextCell(content, value -> Double.toString(value));
    }

    protected static TextCell createTextCell(final String content) {
        return createTextCell(content, identity());
    }

    protected static <T> TextCell createTextCell(final T content, final Function<T, String> transformerContentToString) {
        final String contentAsString = transformerContentToString.apply(content);
        return TextCell.builder()
                .text(contentAsString)
                .borderWidth(CELL_BORDER_WIDTH)
                .build();
    }

    private void finishBuildingPageTable() {
        final Table builtPageTable = this.currentTableBuilder.build();
        this.pageTables.add(builtPageTable);
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