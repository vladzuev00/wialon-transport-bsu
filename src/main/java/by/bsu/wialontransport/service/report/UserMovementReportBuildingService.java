package by.bsu.wialontransport.service.report;

import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.model.DateInterval;
import by.bsu.wialontransport.service.report.exception.UserMovementReportBuildingException;
import by.bsu.wialontransport.service.report.factory.UserMovementReportBuildingContextFactory;
import by.bsu.wialontransport.service.report.model.UserMovementReportBuildingContext;
import by.bsu.wialontransport.service.report.tableappender.AbstractUserMovementReportTableAppender;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static by.bsu.wialontransport.util.PDFUtil.addPage;
import static java.lang.String.format;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Arrays.stream;

@Service
@RequiredArgsConstructor
public final class UserMovementReportBuildingService {
    private static final String DATE_PATTERN = "dd.MM.yyyy";
    private static final DateTimeFormatter DATE_FORMATTER = ofPattern(DATE_PATTERN);

    private static final float INTRODUCTION_PAGE_FONT_SIZE = 16;

    private static final String REPORT_NAME = "Movement report";
    private static final String TEMPLATE_ROW_WITH_USER_EMAIL = "User's email: %s";
    private static final String TEMPLATE_ROW_WITH_DATE_INTERVAL = "Date interval: %s - %s";

    private static final float INTRODUCTION_CONTENT_LINES_LEADING = 14.5f;
    private static final float INTRODUCTION_NEW_LINE_AT_OFFSET_X = 170;
    private static final float INTRODUCTION_NEW_LINE_AT_OFFSET_Y = 450;

    private final UserMovementReportBuildingContextFactory contextFactory;
    private final List<AbstractUserMovementReportTableAppender> tableAppenders;

    public byte[] createReport(final User user, final DateInterval dateInterval) {
        try (final UserMovementReportBuildingContext context = this.contextFactory.create(user, dateInterval)) {
            addIntroduction(context);
            this.appendTablesToContextDocument(context);
            return transformToByteArray(context.getDocument());
        } catch (final IOException cause) {
            throw new UserMovementReportBuildingException(cause);
        }
    }

    private static void addIntroduction(final UserMovementReportBuildingContext context)
            throws IOException {
        final PDDocument document = context.getDocument();
        final PDPage page = addPage(document);
        try (final PDPageContentStream pageContentStream = new PDPageContentStream(document, page)) {
            addIntroductionContentLines(pageContentStream, context);
        }
    }

    private static void addIntroductionContentLines(final PDPageContentStream pageContentStream,
                                                    final UserMovementReportBuildingContext context)
            throws IOException {
        final String rowWithUserEmail = createRowWithUserEmail(context.getUser());
        final String rowWithDateInterval = createRowWithDateInterval(context.getDateInterval());
        addIntroductionContentLines(
                pageContentStream,
                context.getFont(),
                REPORT_NAME,
                rowWithUserEmail,
                rowWithDateInterval
        );
    }

    private static String createRowWithUserEmail(final User user) {
        return format(TEMPLATE_ROW_WITH_USER_EMAIL, user.getEmail());
    }

    private static String createRowWithDateInterval(final DateInterval dateInterval) {
        final String formattedStartDate = DATE_FORMATTER.format(dateInterval.getStart());
        final String formattedEndDate = DATE_FORMATTER.format(dateInterval.getEnd());
        return format(TEMPLATE_ROW_WITH_DATE_INTERVAL, formattedStartDate, formattedEndDate);
    }

    private static void addIntroductionContentLines(final PDPageContentStream pageContentStream,
                                                    final PDFont font,
                                                    final String... lines)
            throws IOException {
        pageContentStream.setLeading(INTRODUCTION_CONTENT_LINES_LEADING);
        pageContentStream.beginText();
        pageContentStream.setFont(font, INTRODUCTION_PAGE_FONT_SIZE);
        pageContentStream.newLineAtOffset(INTRODUCTION_NEW_LINE_AT_OFFSET_X, INTRODUCTION_NEW_LINE_AT_OFFSET_Y);
        addLines(pageContentStream, lines);
        pageContentStream.endText();
    }

    private static void addLines(final PDPageContentStream pageContentStream, final String... lines) {
        stream(lines).forEach(line -> addLine(pageContentStream, line));
    }

    private static void addLine(final PDPageContentStream pageContentStream, final String line) {
        try {
            pageContentStream.showText(line);
            pageContentStream.newLine();
        } catch (final IOException cause) {
            throw new UserMovementReportBuildingException(cause);
        }
    }

    private void appendTablesToContextDocument(final UserMovementReportBuildingContext context) {
        this.tableAppenders.forEach(tableAppender -> tableAppender.appendTableToContextDocument(context));
    }

    private static byte[] transformToByteArray(final PDDocument document)
            throws IOException {
        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            document.save(outputStream);
            return outputStream.toByteArray();
        }
    }

}
