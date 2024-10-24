package by.bsu.wialontransport.protocol.newwing.decoder.data.coordinatecalculator;

import lombok.RequiredArgsConstructor;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;
import static java.lang.Math.abs;
import static java.lang.String.format;
import static java.lang.String.join;

@RequiredArgsConstructor
public abstract class NewWingCoordinateCalculator {
    private static final String POINT = ".";

    private final String integerPartTemplate;
    private final int firstPartIntegerPartNextLastIndex;

    public final double calculate(final short integerPart, final short fractionalPart) {
        final String integerPartAsString = format(integerPartTemplate, abs(integerPart));
        final double abs = parseInt(integerPartAsString.substring(0, firstPartIntegerPartNextLastIndex))
                + parseFloat(
                join(
                        POINT,
                        integerPartAsString.substring(integerPartAsString.length() - 2),
                        Integer.toString(fractionalPart).substring(1)
                )
        ) / 60;
        return integerPart >= 0 ? abs : -1 * abs;
    }
}
