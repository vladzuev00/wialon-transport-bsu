package by.bsu.wialontransport.service.report;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.crud.service.UserService;
import by.bsu.wialontransport.model.DateInterval;
import de.redsix.pdfcompare.CompareResult;
import de.redsix.pdfcompare.PdfComparator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.io.*;
import java.time.LocalDate;

import static org.junit.Assert.assertTrue;

public final class UserMovementReportBuildingIT extends AbstractContextTest {
    private static final String EXPECTED_REPORT_FILE_PATH = "./src/test/resources/user-movement-report/expected-report.pdf";

    @Autowired
    private UserMovementReportBuildingService reportBuildingService;

    @Autowired
    private UserService userService;

    @Test
    @Sql("classpath:sql/user-movement-report.sql")
    public void reportShouldBeCreated()
            throws IOException {
        final Long givenUserId = 255L;
        final User givenUser = this.userService.findById(givenUserId).orElseThrow();

        final DateInterval givenDateInterval = new DateInterval(
                LocalDate.of(2019, 10, 24),
                LocalDate.of(2019, 10, 25)
        );

        final byte[] actual = this.reportBuildingService.createReport(givenUser, givenDateInterval);
        checkActualCreatedReport(actual);
    }

    private static void checkActualCreatedReport(final byte[] actual)
            throws IOException {
        try (final InputStream actualAsStream = new ByteArrayInputStream(actual);
             final InputStream expectedAsStream = new FileInputStream(EXPECTED_REPORT_FILE_PATH)) {
            final CompareResult compareResult = new PdfComparator<>(expectedAsStream, actualAsStream).compare();
            assertTrue(compareResult.isEqual());
        }
    }
}
