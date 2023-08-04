package by.bsu.wialontransport.util;

import lombok.experimental.UtilityClass;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.cell.AbstractCell;
import org.vandeseer.easytable.structure.cell.TextCell;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

@UtilityClass
public final class PDFTestUtil {

    public static List<String> findCellContents(final Stream<Row> rowStream) {
        return rowStream
                .flatMap(PDFTestUtil::findColumnContentsAsStream)
                .toList();
    }

    public static List<String> findCellContents(final AbstractCell[] cells) {
        final Stream<AbstractCell> cellStream = stream(cells);
        return findContentsAsStream(cellStream).toList();
    }

    private static Stream<String> findColumnContentsAsStream(final Row row) {
        final List<AbstractCell> cells = row.getCells();
        final Stream<AbstractCell> cellStream = cells.stream();
        return findContentsAsStream(cellStream);
    }

    private static Stream<String> findContentsAsStream(final Stream<AbstractCell> cellStream) {
        return cellStream
                .map(cell -> (TextCell) cell)
                .map(TextCell::getText);
    }

}
