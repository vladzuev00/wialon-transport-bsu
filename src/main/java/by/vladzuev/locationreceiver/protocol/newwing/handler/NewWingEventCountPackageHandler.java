package by.vladzuev.locationreceiver.protocol.newwing.handler;

import by.vladzuev.locationreceiver.protocol.core.handler.packages.ignored.IgnoredPackageHandler;
import by.vladzuev.locationreceiver.protocol.newwing.model.request.NewWingEventCountPackage;
import org.springframework.stereotype.Component;

@Component
public final class NewWingEventCountPackageHandler extends IgnoredPackageHandler<NewWingEventCountPackage> {

    public NewWingEventCountPackageHandler() {
        super(NewWingEventCountPackage.class);
    }

    @Override
    protected NewWingEventCountPackage createResponse() {
        return new NewWingEventCountPackage();
    }
}
