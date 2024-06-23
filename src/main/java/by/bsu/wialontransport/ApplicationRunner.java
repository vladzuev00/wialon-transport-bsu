package by.bsu.wialontransport;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.springframework.boot.SpringApplication.run;

//TODO: rename configuration to config everywhere
@SpringBootApplication
@ConfigurationPropertiesScan
public class ApplicationRunner {
    public static void main(final String... args) {
        run(ApplicationRunner.class, args);
    }
}
