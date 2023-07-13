package by.bsu.wialontransport.service.report.tabledrawer;

import by.bsu.wialontransport.service.report.model.DistributedTable;
import by.bsu.wialontransport.service.report.tabledrawer.exception.DistributedTableDrawingException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.springframework.stereotype.Service;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.structure.Table;

import java.io.IOException;

import static by.bsu.wialontransport.util.PDFUtil.addPage;

@Service
public final class DistributedTableDrawer {
    private static final float PAGE_TABLE_START_X = 75F;
    private static final float PAGE_TABLE_START_Y = 650F;

    public void draw(final PDDocument document, final DistributedTable table) {
        table.getPageTables()
                .forEach(pageTable -> drawPageTable(document, pageTable));
    }

    private static void drawPageTable(final PDDocument document, final Table pageTable) {
        final PDPage page = addPage(document);
        try (final PDPageContentStream pageContentStream = new PDPageContentStream(document, page)) {
            drawPageTable(pageContentStream, pageTable);
        } catch (final IOException cause) {
            throw new DistributedTableDrawingException(cause);
        }
    }

    private static void drawPageTable(final PDPageContentStream pageContentStream, final Table pageTable) {
        TableDrawer.builder()
                .contentStream(pageContentStream)
                .table(pageTable)
                .startX(PAGE_TABLE_START_X)
                .startY(PAGE_TABLE_START_Y)
                .build()
                .draw();
    }
}
