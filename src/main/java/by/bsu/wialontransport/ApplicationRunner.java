package by.bsu.wialontransport;

import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.model.DateInterval;
import by.bsu.wialontransport.service.report.UserMovementReportBuildingService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
@EnableConfigurationProperties
public class ApplicationRunner {
    public static void main(String[] args) {
        final ConfigurableApplicationContext context = run(ApplicationRunner.class, args);
        final UserMovementReportBuildingService service = context.getBean(UserMovementReportBuildingService.class);
        final User user = User.builder()
                .id(1L)
                .email("vladzuev.00@mail.ru")
                .build();
        final DateInterval dateInterval = new DateInterval(
                LocalDate.of(2020, 3, 7),
                LocalDate.of(2023, 8, 8)
        );
        final byte[] document = service.createReport(user, dateInterval);

        try (final OutputStream outputStream = new FileOutputStream("test.pdf")) {
            outputStream.write(document);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
