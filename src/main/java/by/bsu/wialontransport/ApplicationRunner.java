package by.bsu.wialontransport;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
@EnableConfigurationProperties
@ConfigurationPropertiesScan
public class ApplicationRunner {
    public static void main(String[] args) {
        run(ApplicationRunner.class, args);
    }
}
