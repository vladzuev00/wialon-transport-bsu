package by.vladzuev.locationreceiver.util;

import lombok.experimental.UtilityClass;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.io.IOException;
import java.io.InputStream;

import static java.lang.Thread.currentThread;
import static org.apache.pdfbox.pdmodel.font.PDType0Font.load;

@UtilityClass
public final class FontFactoryUtil {

    public static PDFont loadFont(final PDDocument document, final String fontPath) {
        try (final InputStream inputStream = createFontInputStream(fontPath)) {
            return load(document, inputStream);
        } catch (final IOException cause) {
            throw new FontLoadingException("Loading font by path '%s' was failed".formatted(fontPath));
        }
    }

    private static InputStream createFontInputStream(final String fontPath) {
        return currentThread()
                .getContextClassLoader()
                .getResourceAsStream(fontPath);
    }

    private static final class FontLoadingException extends RuntimeException {

        public FontLoadingException() {

        }

        public FontLoadingException(final String description) {
            super(description);
        }

        public FontLoadingException(final Exception cause) {
            super(cause);
        }

        public FontLoadingException(final String description, final Exception cause) {
            super(description, cause);
        }

    }

}
