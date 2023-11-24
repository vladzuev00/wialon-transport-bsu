package by.bsu.wialontransport.protocol.core.handler.packages.receivingdata;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.kafka.producer.KafkaInboundDataProducer;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.handler.packages.PackageHandler;
import by.bsu.wialontransport.protocol.core.model.packages.Package;
import by.bsu.wialontransport.protocol.core.service.receivingdata.filter.DataFilter;
import by.bsu.wialontransport.protocol.core.service.receivingdata.fixer.DataFixer;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

public abstract class ReceivingDataPackageHandler<PACKAGE extends Package> extends PackageHandler<PACKAGE> {
    private final ContextAttributeManager contextAttributeManager;
    private final DataFilter dataFilter;
    private final KafkaInboundDataProducer kafkaInboundDataProducer;
    private final DataFixer dataFixer;

    public ReceivingDataPackageHandler(final Class<PACKAGE> handledPackageType,
                                       final ContextAttributeManager contextAttributeManager,
                                       final DataFilter dataFilter,
                                       final KafkaInboundDataProducer kafkaInboundDataProducer,
                                       final DataFixer dataFixer) {
        super(handledPackageType);
        this.contextAttributeManager = contextAttributeManager;
        this.dataFilter = dataFilter;
        this.kafkaInboundDataProducer = kafkaInboundDataProducer;
        this.dataFixer = dataFixer;
    }


    //TODO: dateTime and gps coordinate should be defined
    @Override
    protected void handleConcretePackage(final PACKAGE requestPackage, final ChannelHandlerContext context) {
        final List<Data> data = this.extractData(requestPackage);
    }

    protected abstract List<Data> extractData(final PACKAGE requestPackage);
}
