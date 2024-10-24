package by.bsu.wialontransport.protocol.newwing.tempdecoder.data.coordinatecalculator;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class NewWingCoordinateCalculatorTest {
    private final NewWingCoordinateCalculator calculator = new TestNewWingCoordinateCalculator();

    @Test
    public void coordinateShouldBeCalculated() {
        final short givenIntegerPart = 5354;
        final short givenFractionalPart = 1978;

        final double actual = calculator.calculate(givenIntegerPart, givenFractionalPart);
        final double expected = 53.91630172729492;
        assertEquals(expected, actual, 0);
    }

    private static final class TestNewWingCoordinateCalculator extends NewWingCoordinateCalculator {
        private static final String INTEGER_PART_TEMPLATE = "%04d";
        private static final int FIRST_PART_INTEGER_PART_NEXT_LAST_INDEX = 2;

        public TestNewWingCoordinateCalculator() {
            super(INTEGER_PART_TEMPLATE, FIRST_PART_INTEGER_PART_NEXT_LAST_INDEX);
        }
    }
}
