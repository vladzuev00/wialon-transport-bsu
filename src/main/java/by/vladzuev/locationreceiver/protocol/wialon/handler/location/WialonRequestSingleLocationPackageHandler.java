package by.vladzuev.locationreceiver.protocol.wialon.handler.location;

import by.vladzuev.locationreceiver.kafka.producer.data.KafkaInboundLocationProducer;
import by.vladzuev.locationreceiver.protocol.core.handler.packages.location.validator.LocationValidator;
import by.vladzuev.locationreceiver.protocol.core.manager.ContextAttributeManager;
import by.vladzuev.locationreceiver.protocol.core.property.LocationDefaultProperty;
import by.vladzuev.locationreceiver.protocol.wialon.model.packages.location.request.WialonRequestLocationPackage;
import by.vladzuev.locationreceiver.protocol.wialon.model.packages.location.response.WialonResponseSingleLocationPackage;
import org.springframework.stereotype.Component;

import static by.vladzuev.locationreceiver.protocol.wialon.model.packages.location.response.WialonResponseSingleLocationPackage.Status.PACKAGE_FIX_SUCCESS;

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
    protected WialonResponseSingleLocationPackage createResponse(final WialonRequestLocationPackage request) {
        return new WialonResponseSingleLocationPackage(PACKAGE_FIX_SUCCESS);
    }
}
