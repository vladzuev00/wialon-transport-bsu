package by.bsu.wialontransport.service.report.tablebuilder;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.Table.TableBuilder;
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
    private final float[] columnsWidths;
    private final PDFont font;
    private final Integer fontSize;
    private final Color borderColor;
    private final int maxAmountOfRowsInOnePage;
    private final Row nameRow;
    private final Row headerRow;
    private final List<Table> pageTables;
    private int amountOfRowsInCurrentTable;
    private TableBuilder currentTableBuilder;

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
