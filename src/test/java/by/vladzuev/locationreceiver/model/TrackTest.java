package by.vladzuev.locationreceiver.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static by.vladzuev.locationreceiver.model.TempTrack.create;

public final class TrackTest {

    @Test
    public void trackShouldBeCreatedByCoordinateArray() {
        final TempTrack actual = create(
                new CoordinateRequest(1., 2.),
                new CoordinateRequest(3., 4.)
        );
        final TempTrack expected = new TempTrack(
                List.of(
                        new CoordinateRequest(1., 2.),
                        new CoordinateRequest(3., 4.)
                )
        );
        Assert.assertEquals(expected, actual);
    }

}
