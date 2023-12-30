package by.bsu.wialontransport.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static by.bsu.wialontransport.model.TempTrack.create;

public final class TrackTest {

    @Test
    public void trackShouldBeCreatedByCoordinateArray() {
        final TempTrack actual = create(
                new RequestCoordinate(1., 2.),
                new RequestCoordinate(3., 4.)
        );
        final TempTrack expected = new TempTrack(
                List.of(
                        new RequestCoordinate(1., 2.),
                        new RequestCoordinate(3., 4.)
                )
        );
        Assert.assertEquals(expected, actual);
    }

}
