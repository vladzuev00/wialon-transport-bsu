package by.bsu.wialontransport.protocol.core.service.receivingdata;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.kafka.producer.KafkaInboundDataProducer;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.service.receivingdata.exception.NoTrackerInContextException;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.RequestDataPackage;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.ResponseDataPackage.Status;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static by.bsu.wialontransport.protocol.wialon.wialonpackage.data.ResponseDataPackage.Status.ERROR_PACKAGE_STRUCTURE;
import static by.bsu.wialontransport.protocol.wialon.wialonpackage.data.ResponseDataPackage.Status.PACKAGE_FIX_SUCCESS;
import static java.util.Optional.empty;

@Service
@RequiredArgsConstructor
public final class ReceivingDataPackageService {
    private final ContextAttributeManager contextAttributeManager;
    private final DataFilter dataFilter;
    private final KafkaInboundDataProducer kafkaInboundDataProducer;

    public void receive(final RequestDataPackage requestDataPackage, final ChannelHandlerContext context) {
        final Data receivedData = requestDataPackage.getData();
        final Optional<Data> optionalPreviousData = this.contextAttributeManager.findLastData(context);
        final Optional<Data> optionalNewLastData = optionalPreviousData
                .flatMap(previousData -> this.findNewLastDataInCaseExistingPreviousData(receivedData, previousData))
                .or(() -> this.findNewLastDataInCaseNotExistingPreviousData(receivedData));
        final Status responseStatus = optionalNewLastData
                .map(newLastData -> this.handleSuccessReceivedDataAndReturnResponseStatus(newLastData, context))
                .orElse(ERROR_PACKAGE_STRUCTURE);
        context.writeAndFlush(responseStatus);
    }

    private Optional<Data> findNewLastDataInCaseExistingPreviousData(final Data receivedData, final Data previousData) {
        if (this.dataFilter.isValid(receivedData, previousData)) {
            return Optional.of(receivedData);
        } else if (this.dataFilter.isNeedToBeFix(receivedData)) {
            return Optional.of(fixData(receivedData, previousData));
        } else {
            return empty();
        }
    }

    private static Data fixData(final Data fixed, final Data previous) {
        return Data.builder()
                .id(fixed.getId())
                .date(fixed.getDate())
                .time(fixed.getTime())
                .latitude(previous.getLatitude())
                .longitude(previous.getLongitude())
                .speed(fixed.getSpeed())
                .course(fixed.getCourse())
                .altitude(fixed.getAltitude())
                .amountOfSatellites(previous.getAmountOfSatellites())
                .reductionPrecision(fixed.getReductionPrecision())
                .inputs(fixed.getInputs())
                .outputs(fixed.getOutputs())
                .analogInputs(fixed.getAnalogInputs())
                .driverKeyCode(fixed.getDriverKeyCode())
                .parametersByNames(fixed.getParametersByNames())
                .build();
    }

    private Optional<Data> findNewLastDataInCaseNotExistingPreviousData(final Data receivedData) {
        return this.dataFilter.isValid(receivedData) ? Optional.of(receivedData) : empty();
    }

    private Status handleSuccessReceivedDataAndReturnResponseStatus(final Data receivedData,
                                                                    final ChannelHandlerContext context) {
        this.contextAttributeManager.putLastData(context, receivedData);
        this.sendToKafka(receivedData, context);
        return PACKAGE_FIX_SUCCESS;
    }

    private void sendToKafka(final Data receivedData, final ChannelHandlerContext context) {
        final Tracker tracker = this.findTracker(context);
        this.kafkaInboundDataProducer.send(receivedData, tracker);
    }

    private Tracker findTracker(final ChannelHandlerContext context) {
        final Optional<Tracker> optionalTracker = this.contextAttributeManager.findTracker(context);
        return optionalTracker.orElseThrow(NoTrackerInContextException::new);
    }
}
