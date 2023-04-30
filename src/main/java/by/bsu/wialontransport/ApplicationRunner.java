package by.bsu.wialontransport;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
@EnableConfigurationProperties
public class ApplicationRunner {
    public static void main(String[] args) {
        run(ApplicationRunner.class, args);
    }
}
