package by.bsu.wialontransport.protocol.newwing.model.packages.builder;

import by.bsu.wialontransport.protocol.newwing.model.packages.EventCountNewWingPackage;
import lombok.Setter;

@Setter
public final class EventCountNewWingPackageBuilder extends NewWingPackageBuilder<EventCountNewWingPackage> {
    private short eventCount;
    private short frameEventCount;

    @Override
    protected EventCountNewWingPackage build(final int checksum) {
        return new EventCountNewWingPackage(checksum, this.eventCount, this.frameEventCount);
    }
}