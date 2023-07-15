package by.bsu.wialontransport.service.report.factory;

import by.bsu.wialontransport.service.report.model.ReportTable;
import org.springframework.stereotype.Component;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.cell.AbstractCell;

import java.util.List;

import static java.lang.Float.compare;

@Component
public final class ReportTableFactory {

    public ReportTable create(final List<Row> rows) {
        if (rows.isEmpty()) {
            throw new IllegalArgumentException("Rows should not be empty");
        }
        final float[] rowWidths = mapToRowWidths(rows);
        validateRowWidths(rowWidths);
        final float tableWidth = rowWidths[0];
        return new ReportTable(rows, tableWidth);
    }

    private static float[] mapToRowWidths(final List<Row> rows) {
        final float[] rowWidths = new float[rows.size()];
        for (int i = 0; i < rows.size(); i++) {
            rowWidths[i] = findRowWidth(rows.get(i));
        }
        return rowWidths;
    }

    private static float findRowWidth(final Row row) {
        return (float) row.getCells()
                .stream()
                .mapToDouble(AbstractCell::getWidth)
                .sum();
    }

    private static void validateRowWidths(final float[] rowWidths) {
        for (int i = 1; i < rowWidths.length; i++) {
            if (compare(rowWidths[i], rowWidths[0]) != 0) {
                throw new IllegalArgumentException("Widths of report's table rows aren't equal");
            }
        }
    }

}
