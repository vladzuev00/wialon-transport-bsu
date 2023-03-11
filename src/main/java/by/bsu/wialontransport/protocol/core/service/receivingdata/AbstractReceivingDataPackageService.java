package by.bsu.wialontransport.protocol.core.service.receivingdata;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.kafka.producer.KafkaInboundDataProducer;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.service.receivingdata.exception.NoTrackerInContextException;
import by.bsu.wialontransport.protocol.core.service.receivingdata.filter.DataFilter;
import by.bsu.wialontransport.protocol.core.service.receivingdata.fixer.DataFixer;
import by.bsu.wialontransport.protocol.wialon.parameter.DOPParameterDictionary;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.Package;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.request.AbstractRequestDataPackage;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.data.util.Pair;

import java.util.*;

import static by.bsu.wialontransport.crud.dto.Data.createWithTracker;
import static java.util.Arrays.stream;
import static java.util.Optional.empty;
import static java.util.stream.Collectors.toSet;

public abstract class AbstractReceivingDataPackageService<
        RequestPackageType extends AbstractRequestDataPackage,
        ResponsePackageType extends Package> {

    private final ContextAttributeManager contextAttributeManager;
    private final DataFilter dataFilter;
    private final KafkaInboundDataProducer kafkaInboundDataProducer;
    private final DataFixer dataFixer;

    public AbstractReceivingDataPackageService(final ContextAttributeManager contextAttributeManager,
                                               final DataFilter dataFilter,
                                               final KafkaInboundDataProducer kafkaInboundDataProducer,
                                               final DataFixer dataFixer) {
        this.contextAttributeManager = contextAttributeManager;
        this.dataFilter = dataFilter;
        this.kafkaInboundDataProducer = kafkaInboundDataProducer;
        this.dataFixer = dataFixer;
    }

    public final void receive(final RequestPackageType requestPackage, final ChannelHandlerContext context) {
        final List<Data> receivedData = requestPackage.getData();
        final Optional<Data> optionalPreviousData = this.contextAttributeManager.findLastData(context);
        final Pair<Optional<Data>, List<Data>> optionalNewLastDataAndFilteredAndFixedData = optionalPreviousData
                .map(previousData -> this.findNewLastDataAndFilteredAndFixedDataWithTracker(
                        receivedData, previousData, context))
                .orElseGet(() -> this.findNewLastDataAndFilteredAndFixedDataWithTracker(receivedData, context));

        final Optional<Data> optionalNewLastData = optionalNewLastDataAndFilteredAndFixedData.getFirst();
        optionalNewLastData.ifPresent(newLastData -> this.contextAttributeManager.putLastData(context, newLastData));

        final List<Data> filteredAndFixedData = optionalNewLastDataAndFilteredAndFixedData.getSecond();
        filteredAndFixedData.forEach(this.kafkaInboundDataProducer::send);

        final ResponsePackageType responsePackage = this.createResponse(receivedData);
        context.writeAndFlush(responsePackage);
    }

    protected abstract ResponsePackageType createResponse(final List<Data> receivedData);

    private Pair<Optional<Data>, List<Data>> findNewLastDataAndFilteredAndFixedDataWithTracker(
            final List<Data> receivedData, final ChannelHandlerContext context) {
        return this.findNewLastDataAndFilteredAndFixedDataWithTracker(receivedData, null, context);
    }

    private Pair<Optional<Data>, List<Data>> findNewLastDataAndFilteredAndFixedDataWithTracker(
            final List<Data> receivedData, final Data previousData, final ChannelHandlerContext context) {
        final List<Data> fixedData = new ArrayList<>();
        Data previousValidData = previousData;
        for (final Data data : receivedData) {
            final Optional<Data> optionalNewPreviousValidData = previousValidData != null
                    ? this.findNewLastData(data, previousValidData)
                    : this.findNewLastData(data);
            if (optionalNewPreviousValidData.isPresent()) {
                fixedData.add(injectTracker(optionalNewPreviousValidData.get(), context));
                previousValidData = optionalNewPreviousValidData.get();
            }
        }
        final Optional<Data> optionalNewLastData = previousValidData != previousData
                ? Optional.of(previousValidData)
                : empty();
        return Pair.of(optionalNewLastData, fixedData);
    }

    private Optional<Data> findNewLastData(final Data receivedData, final Data previousData) {
        if (this.dataFilter.isValid(receivedData, previousData)) {
            return Optional.of(receivedData);
        } else if (this.dataFilter.isNeedToBeFixed(receivedData, previousData)) {
            return Optional.of(this.dataFixer.fix(receivedData, previousData));
        } else {
            return empty();
        }
    }

    private Optional<Data> findNewLastData(final Data receivedData) {
        return this.dataFilter.isValid(receivedData) ? Optional.of(receivedData) : empty();
    }

    private Data injectTracker(final Data receivedData, final ChannelHandlerContext context) {
        final Tracker tracker = this.findTracker(context);
        return createWithTracker(receivedData, tracker);
    }

    private Tracker findTracker(final ChannelHandlerContext context) {
        final Optional<Tracker> optionalTracker = this.contextAttributeManager.findTracker(context);
        return optionalTracker.orElseThrow(NoTrackerInContextException::new);
    }
}
