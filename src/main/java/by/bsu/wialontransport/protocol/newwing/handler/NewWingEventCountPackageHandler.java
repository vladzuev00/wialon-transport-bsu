package by.bsu.wialontransport.protocol.newwing.handler;

import by.bsu.wialontransport.protocol.core.handler.packages.ignored.IgnoredPackageHandler;
import by.bsu.wialontransport.protocol.newwing.model.request.NewWingEventCountPackage;
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
