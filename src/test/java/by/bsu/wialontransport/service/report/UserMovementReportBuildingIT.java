package by.bsu.wialontransport.service.report;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.crud.service.UserService;
import by.bsu.wialontransport.model.DateInterval;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.io.*;
import java.time.LocalDate;

public final class UserMovementReportBuildingIT extends AbstractContextTest {

    @Autowired
    private UserMovementReportBuildingService reportBuildingService;

    @Autowired
    private UserService userService;

    @Test
    @Sql("classpath:sql/user-movement-report.sql")
    public void reportShouldBeCreated() throws IOException {
        final Long givenUserId = 255L;
        final User givenUser = this.userService.findById(givenUserId).orElseThrow();

        final DateInterval givenDateInterval = new DateInterval(
                LocalDate.of(2019, 10, 24),
                LocalDate.of(2019, 10, 25)
        );

        final byte[] actual = this.reportBuildingService.createReport(givenUser, givenDateInterval);

        try (final OutputStream outputStream = new BufferedOutputStream(new FileOutputStream("temp.pdf"))) {
            outputStream.write(actual);
            outputStream.flush();
        }
    }
}
