package by.bsu.wialontransport.protocol.core.service.receivingdata;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.kafka.producer.KafkaInboundDataProducer;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.service.receivingdata.filter.DataFilter;
import by.bsu.wialontransport.protocol.core.service.receivingdata.fixer.DataFixer;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

public final class ReceivingDataService {
    private final ContextAttributeManager contextAttributeManager;
    private final DataFilter dataFilter;
    private final KafkaInboundDataProducer kafkaInboundDataProducer;
    private final DataFixer dataFixer;

    public ReceivingDataService(final ContextAttributeManager contextAttributeManager,
                                final DataFilter dataFilter,
                                final KafkaInboundDataProducer kafkaInboundDataProducer,
                                final DataFixer dataFixer) {
        this.contextAttributeManager = contextAttributeManager;
        this.dataFilter = dataFilter;
        this.kafkaInboundDataProducer = kafkaInboundDataProducer;
        this.dataFixer = dataFixer;
    }

    public final void receive(final List<Data> receivedData, final ChannelHandlerContext context) {
//        final Optional<Data> optionalPreviousData = this.contextAttributeManager.findLastData(context);
//
//        final List<Data> filteredAndFixedData = optionalPreviousData
//                .map(previousData -> this.findFilteredAndFixedDataWithTracker(receivedData, previousData, context))
//                .orElseGet(() -> this.findFilteredAndFixedDataWithTracker(receivedData, context));
//
//        final Optional<Data> optionalNewLastData = findLast(filteredAndFixedData);
//        optionalNewLastData.ifPresent(newLastData -> {
//            this.contextAttributeManager.putLastData(context, newLastData);
//            filteredAndFixedData.forEach(this.kafkaInboundDataProducer::send);
//        });
//
//        final ResponsePackageType responsePackage = this.createResponse(receivedData);
//        context.writeAndFlush(responsePackage);
    }

//    protected abstract ResponsePackageType createResponse(final List<Data> receivedData);

//    private List<Data> findFilteredAndFixedDataWithTracker(final List<Data> receivedData,
//                                                           final ChannelHandlerContext context) {
//        return this.findFilteredAndFixedDataWithTracker(receivedData, null, context);
//    }
//
//    private List<Data> findFilteredAndFixedDataWithTracker(final List<Data> receivedData,
//                                                           final Data previousData,
//                                                           final ChannelHandlerContext context) {
//        final List<Data> fixedData = new ArrayList<>();
//        Data previousValidData = previousData;
//        for (final Data data : receivedData) {
//            final Optional<Data> optionalNewPreviousValidData = this.findNewLastValidData(data, previousValidData);
//            if (optionalNewPreviousValidData.isPresent()) {
//                final Data dataWithTracker = this.injectTracker(optionalNewPreviousValidData.get(), context);
//                fixedData.add(dataWithTracker);
//                previousValidData = optionalNewPreviousValidData.get();
//            }
//        }
//        return fixedData;
//    }
//
//    private Optional<Data> findNewLastValidData(final Data receivedData, final Data previousData) {
//        return previousData != null
//                ? this.findNewLastValidDataInCaseExistingPreviousData(receivedData, previousData)
//                : this.findNewLastValidDataInCaseNotExistingPreviousData(receivedData);
//    }
//
//    private Optional<Data> findNewLastValidDataInCaseExistingPreviousData(final Data receivedData,
//                                                                          final Data previousData) {
//        if (this.dataFilter.isValid(receivedData, previousData)) {
//            return Optional.of(receivedData);
//        } else if (this.dataFilter.isNeedToBeFixed(receivedData, previousData)) {
//            final Data fixedReceivedData = this.dataFixer.fix(receivedData, previousData);
//            return Optional.of(fixedReceivedData);
//        } else {
//            return empty();
//        }
//    }
//
//    private Optional<Data> findNewLastValidDataInCaseNotExistingPreviousData(final Data receivedData) {
//        return this.dataFilter.isValid(receivedData) ? Optional.of(receivedData) : empty();
//    }
//
//    private Data injectTracker(final Data receivedData, final ChannelHandlerContext context) {
//        final Tracker tracker = this.findTracker(context);
//        return createWithTracker(receivedData, tracker);
//    }
//
//    private Tracker findTracker(final ChannelHandlerContext context) {
//        final Optional<Tracker> optionalTracker = this.contextAttributeManager.findTracker(context);
//        return optionalTracker.orElseThrow(NoTrackerInContextException::new);
//    }
}
