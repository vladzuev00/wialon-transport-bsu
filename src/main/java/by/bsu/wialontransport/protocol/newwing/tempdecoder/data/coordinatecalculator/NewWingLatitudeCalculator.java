package by.bsu.wialontransport.protocol.newwing.tempdecoder.data.coordinatecalculator;

import org.springframework.stereotype.Component;

@Component
public final class NewWingLatitudeCalculator extends NewWingCoordinateCalculator {
    private static final String INTEGER_PART_TEMPLATE = "%04d";
    private static final int FIRST_PART_INTEGER_PART_NEXT_LAST_INDEX = 2;

    public NewWingLatitudeCalculator() {
        super(INTEGER_PART_TEMPLATE, FIRST_PART_INTEGER_PART_NEXT_LAST_INDEX);
    }
}
