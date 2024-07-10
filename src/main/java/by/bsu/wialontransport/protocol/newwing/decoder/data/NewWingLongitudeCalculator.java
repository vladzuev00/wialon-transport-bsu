package by.bsu.wialontransport.protocol.newwing.decoder.data;

import org.springframework.stereotype.Component;

@Component
public final class NewWingLongitudeCalculator extends NewWingCoordinateCalculator {
    public NewWingLongitudeCalculator() {
        super("%05d", 3);
    }
}
