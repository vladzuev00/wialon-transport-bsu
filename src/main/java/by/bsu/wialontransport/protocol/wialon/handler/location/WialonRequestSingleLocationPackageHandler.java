package by.bsu.wialontransport.protocol.wialon.handler.location;

import by.bsu.wialontransport.config.property.LocationDefaultProperty;
import by.bsu.wialontransport.kafka.producer.data.KafkaInboundLocationProducer;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.handler.packages.location.validator.LocationValidator;
import by.bsu.wialontransport.protocol.wialon.model.packages.location.request.WialonRequestLocationPackage;
import by.bsu.wialontransport.protocol.wialon.model.packages.location.response.WialonResponseSingleLocationPackage;
import org.springframework.stereotype.Component;

import static by.bsu.wialontransport.protocol.wialon.model.packages.location.response.WialonResponseSingleLocationPackage.Status.PACKAGE_FIX_SUCCESS;

@Component
public final class WialonRequestSingleLocationPackageHandler extends WialonRequestLocationPackageHandler<WialonRequestLocationPackage> {

    public WialonRequestSingleLocationPackageHandler(final ContextAttributeManager contextAttributeManager,
                                                     final LocationDefaultProperty locationDefaultProperty,
                                                     final LocationValidator locationValidator,
                                                     final KafkaInboundLocationProducer locationProducer) {
        super(
                WialonRequestLocationPackage.class,
                contextAttributeManager,
                locationDefaultProperty,
                locationValidator,
                locationProducer
        );
    }

    @Override
    protected WialonResponseSingleLocationPackage createResponse(final int locationCount) {
        return new WialonResponseSingleLocationPackage(PACKAGE_FIX_SUCCESS);
    }
}
