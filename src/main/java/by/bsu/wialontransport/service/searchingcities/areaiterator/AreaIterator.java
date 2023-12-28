package by.bsu.wialontransport.service.searchingcities.areaiterator;

import by.bsu.wialontransport.model.AreaCoordinate;
import by.bsu.wialontransport.model.RequestCoordinate;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static java.lang.Double.compare;

public final class AreaIterator implements Iterator<RequestCoordinate> {
    private final AreaCoordinate areaCoordinate;
    private final double searchStep;
    private RequestCoordinate current;

    public AreaIterator(final AreaCoordinate areaCoordinate, final double searchStep) {
        this.areaCoordinate = areaCoordinate;
        this.searchStep = searchStep;
        this.current = new RequestCoordinate(
                areaCoordinate.getLeftBottom().getLatitude() - searchStep,
                areaCoordinate.getLeftBottom().getLongitude()
        );
    }

    @Override
    public boolean hasNext() {
        return this.hasNextLatitude() || this.hasNextLongitude();
    }

    @Override
    public RequestCoordinate next() {
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

    private RequestCoordinate nextLatitude() {
        this.current = new RequestCoordinate(
                this.current.getLatitude() + this.searchStep,
                this.current.getLongitude()
        );
        return this.current;
    }

    private RequestCoordinate nextLongitude() {
        this.current = new RequestCoordinate(
                this.areaCoordinate.getLeftBottom().getLatitude(),
                this.current.getLongitude() + this.searchStep
        );
        return this.current;
    }
}
