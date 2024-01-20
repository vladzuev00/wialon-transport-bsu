package by.bsu.wialontransport.protocol.wialon.handler.packages.data;

import by.bsu.wialontransport.configuration.property.DataDefaultPropertyConfiguration;
import by.bsu.wialontransport.kafka.producer.data.KafkaInboundDataProducer;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.handler.packages.receivingdata.ReceivedDataValidator;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request.WialonRequestDataPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.WialonResponseDataPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.WialonResponseDataPackage.Status;

import static by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.WialonResponseDataPackage.Status.ERROR_PACKAGE_STRUCTURE;
import static by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.WialonResponseDataPackage.Status.PACKAGE_FIX_SUCCESS;

public final class WialonRequestDataPackageHandler extends AbstractWialonRequestDataPackageHandler<WialonRequestDataPackage> {

    public WialonRequestDataPackageHandler(final DataDefaultPropertyConfiguration dataDefaultPropertyConfiguration,
                                           final ContextAttributeManager contextAttributeManager,
                                           final ReceivedDataValidator receivedDataValidator,
                                           final KafkaInboundDataProducer kafkaInboundDataProducer) {
        super(
                WialonRequestDataPackage.class,
                dataDefaultPropertyConfiguration,
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
