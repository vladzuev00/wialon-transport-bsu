package by.vladzuev.locationreceiver.util;

import lombok.experimental.UtilityClass;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

@UtilityClass
public final class PDFUtil {

    public static PDPage addPage(final PDDocument document) {
        final PDPage page = new PDPage();
        document.addPage(page);
        return page;
    }

}
