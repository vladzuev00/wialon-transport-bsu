package by.vladzuev.locationreceiver.protocol.wialon.handler.location;

import by.vladzuev.locationreceiver.kafka.producer.data.KafkaInboundLocationProducer;
import by.vladzuev.locationreceiver.protocol.core.handler.packages.location.validator.LocationValidator;
import by.vladzuev.locationreceiver.protocol.core.manager.ContextAttributeManager;
import by.vladzuev.locationreceiver.protocol.core.property.LocationDefaultProperty;
import by.vladzuev.locationreceiver.protocol.wialon.model.location.request.WialonRequestBulkLocationPackage;
import by.vladzuev.locationreceiver.protocol.wialon.model.location.response.WialonResponseBulkLocationPackage;
import org.springframework.stereotype.Component;

@Component
public final class WialonRequestBulkLocationPackageHandler extends WialonRequestLocationPackageHandler<WialonRequestBulkLocationPackage> {

    public WialonRequestBulkLocationPackageHandler(final ContextAttributeManager contextAttributeManager,
                                                   final LocationDefaultProperty locationDefaultProperty,
                                                   final LocationValidator locationValidator,
                                                   final KafkaInboundLocationProducer locationProducer) {
        super(
                WialonRequestBulkLocationPackage.class,
                contextAttributeManager,
                locationDefaultProperty,
                locationValidator,
                locationProducer
        );
    }

    @Override
    protected WialonResponseBulkLocationPackage createResponse(final WialonRequestBulkLocationPackage request) {
        return new WialonResponseBulkLocationPackage(request.getLocations().size());
    }
}
