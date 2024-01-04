package by.bsu.wialontransport.service.searchingcities.areaiterator;

import by.bsu.wialontransport.model.AreaCoordinate;
import by.bsu.wialontransport.model.Coordinate;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static java.lang.Double.compare;

public final class AreaIterator implements Iterator<Coordinate> {
    private final AreaCoordinate areaCoordinate;
    private final double searchStep;
    private Coordinate currentCoordinate;

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
    public Coordinate next() {
        if (hasNextLatitude()) {
            return nextLatitude();
        } else if (hasNextLongitude()) {
            return nextLongitude();
        }
        throw new NoSuchElementException();
    }

    private static Coordinate findInitialCurrentCoordinate(final AreaCoordinate areaCoordinate,
                                                           final double searchStep) {
        final double latitude = areaCoordinate.getLeftBottom().getLatitude() - searchStep;
        final double longitude = areaCoordinate.getLeftBottom().getLongitude();
        return new Coordinate(latitude, longitude);
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

    private Coordinate nextLatitude() {
        currentCoordinate = new Coordinate(
                currentCoordinate.getLatitude() + searchStep,
                currentCoordinate.getLongitude()
        );
        return currentCoordinate;
    }

    private Coordinate nextLongitude() {
        currentCoordinate = new Coordinate(
                areaCoordinate.getLeftBottom().getLatitude(),
                currentCoordinate.getLongitude() + searchStep
        );
        return currentCoordinate;
    }
}
