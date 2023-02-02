package by.bsu.wialontransport.configuration.property;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Getter
public class DataValidationProperty {
    private int minValidAmountOfSatellites;
    private int maxValidAmountSatellite;
    private LocalDateTime minValidDateTime;
    private int deltaSecondsFromNowMaxAllowableValidDateTime;
    private int minValidDOP;
    private int maxValidDOP;
}