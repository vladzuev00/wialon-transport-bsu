package by.bsu.wialontransport.protocol.newwing.model.packages;

import lombok.Getter;

@Getter
public final class EventCountNewWingPackage extends NewWingPackage {
    private final short eventCount;
    private final short frameEventCount;

    public EventCountNewWingPackage(final int checksum, final short eventCount, final short frameEventCount) {
        super(checksum);
        this.eventCount = eventCount;
        this.frameEventCount = frameEventCount;
    }
}