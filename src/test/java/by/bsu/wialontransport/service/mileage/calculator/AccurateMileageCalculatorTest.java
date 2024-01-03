package by.bsu.wialontransport.service.mileage.calculator;

import by.bsu.wialontransport.service.mileage.calculator.MileageCalculator.TrackSlicesCreatingContext;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public final class AccurateMileageCalculatorTest {
    private final AccurateMileageCalculator calculator = new AccurateMileageCalculator(
            null,
            null,
            null,
            null
    );

    @Test(expected = UnsupportedOperationException.class)
    public void trackSlicesShouldNotBeCreated() {
        final TrackSlicesCreatingContext givenContext = mock(TrackSlicesCreatingContext.class);

        calculator.createTrackSlices(givenContext);
    }
}
