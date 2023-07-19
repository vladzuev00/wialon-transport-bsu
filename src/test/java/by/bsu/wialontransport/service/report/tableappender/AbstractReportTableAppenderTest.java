package by.bsu.wialontransport.service.report.tableappender;

import by.bsu.wialontransport.service.report.model.TableRowMetaData;
import by.bsu.wialontransport.service.report.model.UserMovementReportBuildingContext;
import by.bsu.wialontransport.service.report.tabledrawer.DistributedReportTableDrawer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.cell.AbstractCell;

import java.util.List;

import static by.bsu.wialontransport.util.PDFCellFactoryUtil.createTextCell;

@RunWith(MockitoJUnitRunner.class)
public final class AbstractReportTableAppenderTest {

    @Mock
    private DistributedReportTableDrawer mockedReportTableDrawer;

    @Test
    public void tableShouldBeAppendedToDocument() {
        final List<Row> givenContentRows = List.of(createRow(), createRow(), createRow());
        final AbstractCell[] givenHeaderRowCells = {
                createTextCell("first-header-column"),
                createTextCell("second-header-column"),
                createTextCell("third-header-column")
        };
//        final TestReportTableAppender givenTableAppender = new TestReportTableAppender();
        throw new RuntimeException();
    }

    private static Row createRow() {
        return Row.builder()
                .build();
    }

    private static final class TestReportTableAppender extends AbstractReportTableAppender {
        private final List<Row> contentRows;
        private final AbstractCell[] headerRowCells;

        public TestReportTableAppender(final DistributedReportTableDrawer tableDrawer,
                                       final float[] columnWidths,
                                       final TableRowMetaData nameRowMetaData,
                                       final TableRowMetaData headerRowMetaData,
                                       final String tableName,
                                       final List<Row> contentRows,
                                       final AbstractCell[] headerRowCells) {
            super(tableDrawer, columnWidths, nameRowMetaData, headerRowMetaData, tableName);
            this.contentRows = contentRows;
            this.headerRowCells = headerRowCells;
        }

        @Override
        protected List<Row> createContentRows(UserMovementReportBuildingContext context) {
            return this.contentRows;
        }

        @Override
        protected AbstractCell[] createHeaderRowCells() {
            return this.headerRowCells;
        }

    }
}
