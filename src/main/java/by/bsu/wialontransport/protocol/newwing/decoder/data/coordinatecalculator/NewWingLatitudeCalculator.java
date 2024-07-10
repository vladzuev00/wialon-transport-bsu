package by.bsu.wialontransport.protocol.newwing.decoder.data.coordinatecalculator;

import org.springframework.stereotype.Component;

@Component
public final class NewWingLatitudeCalculator extends NewWingCoordinateCalculator {

    public NewWingLatitudeCalculator() {
        super("%04d", 2);
    }
}
