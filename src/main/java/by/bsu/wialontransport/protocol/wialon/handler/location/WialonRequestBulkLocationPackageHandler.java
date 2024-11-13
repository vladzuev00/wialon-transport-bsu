package by.bsu.wialontransport.protocol.wialon.handler.location;

import by.bsu.wialontransport.config.property.LocationDefaultProperty;
import by.bsu.wialontransport.kafka.producer.data.KafkaInboundLocationProducer;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.handler.packages.location.validator.LocationValidator;
import by.bsu.wialontransport.protocol.wialon.model.packages.location.request.WialonRequestBulkLocationPackage;
import by.bsu.wialontransport.protocol.wialon.model.packages.location.response.WialonResponseBulkLocationPackage;
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
    protected WialonResponseBulkLocationPackage createResponse(final int locationCount) {
        return new WialonResponseBulkLocationPackage(locationCount);
    }
}
