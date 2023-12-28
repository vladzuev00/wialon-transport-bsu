package by.bsu.wialontransport.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static by.bsu.wialontransport.model.Track.create;

public final class TrackTest {

    @Test
    public void trackShouldBeCreatedByCoordinateArray() {
        final Track actual = create(
                new RequestCoordinate(1., 2.),
                new RequestCoordinate(3., 4.)
        );
        final Track expected = new Track(
                List.of(
                        new RequestCoordinate(1., 2.),
                        new RequestCoordinate(3., 4.)
                )
        );
        Assert.assertEquals(expected, actual);
    }

}
