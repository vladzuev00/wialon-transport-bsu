package by.bsu.wialontransport.protocol.newwing.handler;

import by.bsu.wialontransport.crud.service.LocationService;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.contextmanager.ChannelHandlerContextManager;
import by.bsu.wialontransport.protocol.core.handler.packages.login.UnprotectedLoginPackageHandler;
import by.bsu.wialontransport.protocol.core.handler.packages.login.factory.TrackerImeiFactory;
import by.bsu.wialontransport.protocol.newwing.model.request.NewWingLoginPackage;
import by.bsu.wialontransport.protocol.newwing.model.response.NewWingFailureResponsePackage;
import by.bsu.wialontransport.protocol.newwing.model.response.NewWingSuccessResponsePackage;
import org.springframework.stereotype.Component;

@Component
public final class NewWingLoginPackageHandler extends UnprotectedLoginPackageHandler<NewWingLoginPackage> {

    public NewWingLoginPackageHandler(final TrackerImeiFactory imeiFactory,
                                      final ContextAttributeManager contextAttributeManager,
                                      final TrackerService trackerService,
                                      final ChannelHandlerContextManager contextManager,
                                      final LocationService locationService) {
        super(
                NewWingLoginPackage.class,
                imeiFactory,
                contextAttributeManager,
                trackerService,
                contextManager,
                locationService
        );
    }

    @Override
    protected NewWingFailureResponsePackage createNoSuchImeiResponse() {
        return new NewWingFailureResponsePackage();
    }

    @Override
    protected NewWingSuccessResponsePackage createSuccessResponse() {
        return new NewWingSuccessResponsePackage();
    }
}
