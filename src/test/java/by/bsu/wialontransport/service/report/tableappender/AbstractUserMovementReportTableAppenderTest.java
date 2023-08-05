package by.bsu.wialontransport.service.report.tableappender;

import by.bsu.wialontransport.service.report.model.TableRowMetaData;
import by.bsu.wialontransport.service.report.model.TrackerMovement;
import by.bsu.wialontransport.service.report.tabledrawer.DistributedReportTableDrawer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.cell.AbstractCell;

import java.util.stream.Stream;

@RunWith(MockitoJUnitRunner.class)
public final class AbstractUserMovementReportTableAppenderTest {

    @Mock
    private DistributedReportTableDrawer mockedTableDrawer;

    @Test
    public void tableShouldBeAppendedToContextDocument() {
        throw new RuntimeException();
    }

    private static final class TestUserMovementReportTableAppended extends AbstractUserMovementReportTableAppender {

        public TestUserMovementReportTableAppended(final DistributedReportTableDrawer tableDrawer,
                                                   final float[] columnWidths,
                                                   final TableRowMetaData nameRowMetaData,
                                                   final TableRowMetaData headerRowMetaData,
                                                   final String tableName) {
            super(tableDrawer, columnWidths, nameRowMetaData, headerRowMetaData, tableName);
        }

        @Override
        protected Stream<Row> createContentRowStream(final TrackerMovement movement) {
            return null;
        }

        @Override
        protected AbstractCell[] createHeaderRowCells() {
            return new AbstractCell[0];
        }

    }

}
