package by.bsu.wialontransport.service.report.factory;

import by.bsu.wialontransport.service.report.model.ReportTable;
import org.springframework.stereotype.Component;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.cell.AbstractCell;

import java.util.List;

import static java.lang.Double.compare;

@Component
public final class ReportTableSourceFactory {

    public ReportTable create(final List<Row> rows) {
        if (rows.isEmpty()) {
            throw new IllegalArgumentException("Rows should not be empty");
        }
        final double[] rowWidths = mapToRowWidths(rows);
        validateRowWidths(rowWidths);
        final double tableWidth = rowWidths[0];
        return new ReportTable(rows, tableWidth);
    }

    private static double[] mapToRowWidths(final List<Row> rows) {
        return rows.stream()
                .mapToDouble(ReportTableSourceFactory::findRowWidth)
                .toArray();
    }

    private static double findRowWidth(final Row row) {
        return row.getCells()
                .stream()
                .mapToDouble(AbstractCell::getWidth)
                .sum();
    }

    private static void validateRowWidths(final double[] rowWidths) {
        for (int i = 1; i < rowWidths.length; i++) {
            if (compare(rowWidths[i], rowWidths[0]) != 0) {
                throw new IllegalArgumentException("Widths of report's table rows aren't equal");
            }
        }
    }

}
