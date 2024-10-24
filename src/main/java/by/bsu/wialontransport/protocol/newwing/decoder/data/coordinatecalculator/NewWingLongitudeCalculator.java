package by.bsu.wialontransport.protocol.newwing.decoder.data.coordinatecalculator;

import org.springframework.stereotype.Component;

@Component
public final class NewWingLongitudeCalculator extends NewWingCoordinateCalculator {
    private static final String INTEGER_PART_TEMPLATE = "%05d";
    private static final int FIRST_PART_INTEGER_PART_NEXT_LAST_INDEX = 3;

    public NewWingLongitudeCalculator() {
        super(INTEGER_PART_TEMPLATE, FIRST_PART_INTEGER_PART_NEXT_LAST_INDEX);
    }
}
