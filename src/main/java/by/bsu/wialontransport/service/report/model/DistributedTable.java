package by.bsu.wialontransport.service.report.model;

import lombok.Value;
import org.vandeseer.easytable.structure.Table;

import java.util.Collection;
import java.util.List;

@Value
public class DistributedTable {
    List<Table> pageTables;

    public static DistributedTable unite(final List<DistributedTable> tables) {
        final List<Table> unitedPageTables = unitePageTables(tables);
        return new DistributedTable(unitedPageTables);
    }

    private static List<Table> unitePageTables(final List<DistributedTable> tables) {
        return tables.stream()
                .map(DistributedTable::getPageTables)
                .flatMap(Collection::stream)
                .toList();
    }
}
