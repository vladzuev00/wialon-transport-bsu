package by.bsu.wialontransport.protocol.newwing.model.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class NewWingEventCountPackage extends NewWingRequestPackage {
    private final short eventCount;
    private final short frameEventCount;

    public NewWingEventCountPackage(final int checksum, final short eventCount, final short frameEventCount) {
        super(checksum);
        this.eventCount = eventCount;
        this.frameEventCount = frameEventCount;
    }
}
