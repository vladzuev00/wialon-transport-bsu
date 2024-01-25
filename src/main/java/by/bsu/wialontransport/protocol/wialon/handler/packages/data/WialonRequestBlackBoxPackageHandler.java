package by.bsu.wialontransport.protocol.wialon.handler.packages.data;

import by.bsu.wialontransport.configuration.property.DataDefaultPropertyConfiguration;
import by.bsu.wialontransport.kafka.producer.data.KafkaInboundDataProducer;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.handler.packages.receivingdata.ReceivedDataValidator;
import by.bsu.wialontransport.protocol.wialon.model.packages.data.request.WialonRequestBlackBoxPackage;
import by.bsu.wialontransport.protocol.wialon.model.packages.data.response.WialonResponseBlackBoxPackage;

public final class WialonRequestBlackBoxPackageHandler extends AbstractWialonRequestDataPackageHandler<
        WialonRequestBlackBoxPackage
        > {

    public WialonRequestBlackBoxPackageHandler(final DataDefaultPropertyConfiguration dataDefaultPropertyConfiguration,
                                               final ContextAttributeManager contextAttributeManager,
                                               final ReceivedDataValidator receivedDataValidator,
                                               final KafkaInboundDataProducer kafkaInboundDataProducer) {
        super(
                WialonRequestBlackBoxPackage.class,
                dataDefaultPropertyConfiguration,
                contextAttributeManager,
                receivedDataValidator,
                kafkaInboundDataProducer
        );
    }

    @Override
    protected WialonResponseBlackBoxPackage createResponse(final int receivedDataCount) {
        return new WialonResponseBlackBoxPackage(receivedDataCount);
    }
}
