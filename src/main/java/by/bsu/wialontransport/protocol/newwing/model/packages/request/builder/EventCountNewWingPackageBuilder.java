package by.bsu.wialontransport.protocol.newwing.model.packages.request.builder;

import by.bsu.wialontransport.protocol.newwing.model.packages.request.NewWingEventCountPackage;
import lombok.Setter;

@Setter
public final class EventCountNewWingPackageBuilder extends NewWingRequestPackageBuilder<NewWingEventCountPackage> {
    private short eventCount;
    private short frameEventCount;

    @Override
    protected NewWingEventCountPackage build(final int checksum) {
        return new NewWingEventCountPackage(checksum, this.eventCount, this.frameEventCount);
    }
}