package by.bsu.wialontransport.kafka.consumer;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.service.AddressService;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.kafka.consumer.exception.DataConsumingException;
import by.bsu.wialontransport.kafka.transportable.TransportableSavedData;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;

@Slf4j
@Component
public final class KafkaSavedDataConsumer extends AbstractKafkaDataConsumer {
    private final AddressService addressService;

    public KafkaSavedDataConsumer(final TrackerService trackerService,
                                  final AddressService addressService) {
        super(trackerService);
        this.addressService = addressService;
    }

    @Override
    @KafkaListener(
            topics = "${kafka.topic.saved-data.name}",
            groupId = "${kafka.topic.saved-data.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactorySavedData"
    )
    public void consume(final List<ConsumerRecord<Long, GenericRecord>> consumerRecords) {
        super.consume(consumerRecords);
    }

    @Override
    protected void processData(final List<Data> data) {
        log.info("Consuming saved data: {}", data);
    }

    @Override
    protected Data createData(final Long id,
                              final LocalDate date,
                              final LocalTime time,
                              final Data.Latitude latitude,
                              final Data.Longitude longitude,
                              final int speed,
                              final int course,
                              final int altitude,
                              final int amountOfSatellites,
                              final double reductionPrecision,
                              final int inputs,
                              final int outputs,
                              final double[] analogInputs,
                              final String driverKeyCode,
                              final Map<String, Parameter> parametersByNames,
                              final Tracker tracker,
                              final GenericRecord genericRecord) {
        return new Data(
                id,
                date,
                time,
                latitude,
                longitude,
                speed,
                course,
                altitude,
                amountOfSatellites,
                reductionPrecision,
                inputs,
                outputs,
                analogInputs,
                driverKeyCode,
                parametersByNames,
                tracker,
                this.extractAddress(genericRecord)
        );
    }

    private Address extractAddress(final GenericRecord genericRecord) {
        final Long addressId = extractAddressId(genericRecord);
        final Optional<Address> optionalAddress = this.addressService.findById(addressId);
        return optionalAddress.orElseThrow(
                () -> new DataConsumingException(
                        format("Impossible to find address with id '%d'.", addressId)
                )
        );
    }

    private Long extractAddressId(final GenericRecord genericRecord) {
        return extractValue(genericRecord, TransportableSavedData.Fields.addressId);
    }
}
