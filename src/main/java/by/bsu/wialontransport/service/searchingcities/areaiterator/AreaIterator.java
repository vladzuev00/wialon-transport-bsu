package by.bsu.wialontransport.service.searchingcities.areaiterator;

import by.bsu.wialontransport.model.AreaCoordinate;
import by.bsu.wialontransport.model.Coordinate;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static java.lang.Double.compare;

public final class AreaIterator implements Iterator<Coordinate> {
    private final AreaCoordinate areaCoordinate;
    private final double searchStep;
    private Coordinate current;

    public AreaIterator(final AreaCoordinate areaCoordinate, final double searchStep) {
        this.areaCoordinate = areaCoordinate;
        this.searchStep = searchStep;
        this.current = new Coordinate(
                areaCoordinate.getLeftBottom().getLatitude() - searchStep,
                areaCoordinate.getLeftBottom().getLongitude()
        );
    }

    @Override
    public boolean hasNext() {
        return this.hasNextLatitude() || this.hasNextLongitude();
    }

    @Override
    public Coordinate next() {
        if (this.hasNextLatitude()) {
            return this.nextLatitude();
        } else if (this.hasNextLongitude()) {
            return this.nextLongitude();
        }
        throw new NoSuchElementException();
    }

    private boolean hasNextLatitude() {
        return compare(
                this.current.getLatitude() + this.searchStep,
                this.areaCoordinate.getRightUpper().getLatitude()
        ) <= 0;
    }

    private boolean hasNextLongitude() {
        return compare(
                this.current.getLongitude() + this.searchStep,
                this.areaCoordinate.getRightUpper().getLongitude()
        ) <= 0;
    }

    private Coordinate nextLatitude() {
        this.current = new Coordinate(
                this.current.getLatitude() + this.searchStep,
                this.current.getLongitude()
        );
        return this.current;
    }

    private Coordinate nextLongitude() {
        this.current = new Coordinate(
                this.areaCoordinate.getLeftBottom().getLatitude(),
                this.current.getLongitude() + this.searchStep
        );
        return this.current;
    }
}
