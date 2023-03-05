package by.bsu.wialontransport.protocol.core.service.receivingdatapackage;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.kafka.producer.KafkaInboundDataProducer;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.service.receivingdatapackage.exception.NoTrackerInContextException;
import by.bsu.wialontransport.protocol.core.service.receivingdatapackage.validator.DataFilter;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.RequestDataPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.ResponseDataPackage;
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
        final Tracker tracker = this.findTracker(context);
        final Optional<Data> optionalPreviousData = this.contextAttributeManager.findLastData(context);
        final boolean dataShouldBeSkipped = optionalPreviousData
                .map(previousData -> this.dataFilter.isNeedToBeSkipped(receivedData, previousData))
                .orElseGet(() -> this.dataFilter.isNeedToBeSkipped(receivedData));
        if (!dataShouldBeSkipped) {
            final Optional<Data> optionalNewLastData = optionalPreviousData
                    .map(previousData -> this.findDataWithCalculationsAndFixIfNotValid(receivedData, previousData))
                    .or(() -> this.findDataWithCalculationsIfDataIsValid(receivedData));
            optionalNewLastData.ifPresentOrElse(newLastData -> {
                        this.contextAttributeManager.putLastData(context, newLastData);
                        this.kafkaInboundDataProducer.send(newLastData, tracker);
                        context.writeAndFlush(new ResponseDataPackage(PACKAGE_FIX_SUCCESS));
                    },
                    () -> context.writeAndFlush(new ResponseDataPackage(ERROR_PACKAGE_STRUCTURE)));
        } else {
            context.writeAndFlush(new ResponseDataPackage(ERROR_PACKAGE_STRUCTURE));
        }
    }

    private Tracker findTracker(final ChannelHandlerContext context) {
        final Optional<Tracker> optionalTracker = this.contextAttributeManager.findTracker(context);
        return optionalTracker.orElseThrow(NoTrackerInContextException::new);
    }

    private Data findDataWithCalculationsAndFixIfNotValid(final Data received, final Data previous) {
        return this.dataFilter.isValid(received, previous)
                ? this.createDataWithCalculations(received, previous)
                : this.createFixedDataWithCalculations(received, previous);
    }

    private Optional<Data> findDataWithCalculationsIfDataIsValid(final Data receivedData) {
        return this.dataFilter.isValid(receivedData)
                ? Optional.of(this.createDataWithCalculations(receivedData))
                : empty();
    }

    private Data createDataWithCalculations(final Data received) {
        //nal DataCalculations dataCalculations = this.dataCalculationsFactory.create(received);
        //return new Data(received, dataCalculations);
        return null;
    }

    private Data createDataWithCalculations(final Data received, final Data previous) {
        //final DataCalculations dataCalculations = this.dataCalculationsFactory.create(received, previous);
        //return new Data(received, dataCalculations);
        return null;
    }

    private Data createFixedDataWithCalculations(final Data received, final Data previous) {
        final Data fixedReceivedData = fixData(received, previous);
        return this.createDataWithCalculations(fixedReceivedData, previous);
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
}
