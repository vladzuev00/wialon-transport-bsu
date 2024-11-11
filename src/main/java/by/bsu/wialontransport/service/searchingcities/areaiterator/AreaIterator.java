package by.bsu.wialontransport.service.searchingcities.areaiterator;

import by.bsu.wialontransport.model.AreaCoordinate;
import by.bsu.wialontransport.model.GpsCoordinate;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static java.lang.Double.compare;

public final class AreaIterator implements Iterator<GpsCoordinate> {
    private final AreaCoordinate areaCoordinate;
    private final double searchStep;
    private GpsCoordinate currentCoordinate;

    public AreaIterator(final AreaCoordinate areaCoordinate, final double searchStep) {
        this.areaCoordinate = areaCoordinate;
        this.searchStep = searchStep;
        currentCoordinate = findInitialCurrentCoordinate(areaCoordinate, searchStep);
    }

    @Override
    public boolean hasNext() {
        return hasNextLatitude() || hasNextLongitude();
    }

    @Override
    public GpsCoordinate next() {
        if (hasNextLatitude()) {
            return nextLatitude();
        } else if (hasNextLongitude()) {
            return nextLongitude();
        }
        throw new NoSuchElementException();
    }

    private static GpsCoordinate findInitialCurrentCoordinate(final AreaCoordinate areaCoordinate,
                                                              final double searchStep) {
        final double latitude = areaCoordinate.getLeftBottom().getLatitude() - searchStep;
        final double longitude = areaCoordinate.getLeftBottom().getLongitude();
        return new GpsCoordinate(latitude, longitude);
    }

    private boolean hasNextLatitude() {
        return compare(
                currentCoordinate.getLatitude() + searchStep,
                areaCoordinate.getRightUpper().getLatitude()
        ) <= 0;
    }

    private boolean hasNextLongitude() {
        return compare(
                currentCoordinate.getLongitude() + searchStep,
                areaCoordinate.getRightUpper().getLongitude()
        ) <= 0;
    }

    private GpsCoordinate nextLatitude() {
        currentCoordinate = new GpsCoordinate(
                currentCoordinate.getLatitude() + searchStep,
                currentCoordinate.getLongitude()
        );
        return currentCoordinate;
    }

    private GpsCoordinate nextLongitude() {
        currentCoordinate = new GpsCoordinate(
                areaCoordinate.getLeftBottom().getLatitude(),
                currentCoordinate.getLongitude() + searchStep
        );
        return currentCoordinate;
    }
}
