package by.bsu.wialontransport.protocol.newwing.model.packages.request.builder;

import by.bsu.wialontransport.protocol.newwing.model.packages.request.EventCountNewWingPackage;
import lombok.Setter;

@Setter
public final class EventCountNewWingPackageBuilder extends NewWingRequestPackageBuilder<EventCountNewWingPackage> {
    private short eventCount;
    private short frameEventCount;

    @Override
    protected EventCountNewWingPackage build(final int checksum) {
        return new EventCountNewWingPackage(checksum, this.eventCount, this.frameEventCount);
    }
}