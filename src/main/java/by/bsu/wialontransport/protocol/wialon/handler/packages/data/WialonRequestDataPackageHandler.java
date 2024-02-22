package by.bsu.wialontransport.protocol.wialon.handler.packages.data;

import by.bsu.wialontransport.config.property.DataDefaultPropertyConfig;
import by.bsu.wialontransport.kafka.producer.data.KafkaInboundDataProducer;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.handler.packages.receivingdata.ReceivedDataValidator;
import by.bsu.wialontransport.protocol.wialon.model.packages.data.request.WialonRequestDataPackage;
import by.bsu.wialontransport.protocol.wialon.model.packages.data.response.WialonResponseDataPackage;
import by.bsu.wialontransport.protocol.wialon.model.packages.data.response.WialonResponseDataPackage.Status;
import org.springframework.stereotype.Component;

import static by.bsu.wialontransport.protocol.wialon.model.packages.data.response.WialonResponseDataPackage.Status.ERROR_PACKAGE_STRUCTURE;
import static by.bsu.wialontransport.protocol.wialon.model.packages.data.response.WialonResponseDataPackage.Status.PACKAGE_FIX_SUCCESS;

@Component
public final class WialonRequestDataPackageHandler extends AbstractWialonRequestDataPackageHandler<WialonRequestDataPackage> {

    public WialonRequestDataPackageHandler(final DataDefaultPropertyConfig dataDefaultPropertyConfig,
                                           final ContextAttributeManager contextAttributeManager,
                                           final ReceivedDataValidator receivedDataValidator,
                                           final KafkaInboundDataProducer kafkaInboundDataProducer) {
        super(
                WialonRequestDataPackage.class,
                dataDefaultPropertyConfig,
                contextAttributeManager,
                receivedDataValidator,
                kafkaInboundDataProducer
        );
    }

    @Override
    protected WialonResponseDataPackage createResponse(final int receivedDataCount) {
        final Status status = receivedDataCount == 1 ? PACKAGE_FIX_SUCCESS : ERROR_PACKAGE_STRUCTURE;
        return new WialonResponseDataPackage(status);
    }
}
