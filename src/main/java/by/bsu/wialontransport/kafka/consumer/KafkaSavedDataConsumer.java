package by.bsu.wialontransport.kafka.consumer;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.service.AddressService;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.kafka.consumer.exception.DataConsumingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static by.bsu.wialontransport.kafka.transportable.TransportableSavedData.Fields.addressId;
import static by.bsu.wialontransport.kafka.transportable.TransportableSavedData.Fields.id;
import static java.lang.String.format;

@Slf4j
@Component
public final class KafkaSavedDataConsumer extends AbstractKafkaDataConsumer {
    private final AddressService addressService;

    public KafkaSavedDataConsumer(final TrackerService trackerService, final AddressService addressService) {
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
    protected Data mapToData(final GenericRecord genericRecord) {
        final LocalDateTime dateTime = extractDateTime(genericRecord);
//        return new Data(
//                extractId(genericRecord),
//                dateTime.toLocalDate(),
//                dateTime.toLocalTime(),
//                super.extractLatitude(genericRecord),
//                super.extractLongitude(genericRecord),
//                extractSpeed(genericRecord),
//                extractCourse(genericRecord),
//                extractAltitude(genericRecord),
//                extractAmountOfSatellites(genericRecord),
//                extractReductionPrecision(genericRecord),
//                extractInputs(genericRecord),
//                extractOutputs(genericRecord),
//                extractAnalogInputs(genericRecord),
//                extractDriverKeyCode(genericRecord),
//                super.extractParametersByNames(genericRecord),
//                super.extractTracker(genericRecord),
//                this.extractAddress(genericRecord)
//        );
        return null;
    }

    @Override
    protected void processData(final List<Data> data) {
        log.info("Consuming saved data: {}", data);
    }

    private static Long extractId(final GenericRecord genericRecord) {
        return extractValue(genericRecord, id);
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

    private static Long extractAddressId(final GenericRecord genericRecord) {
        return extractValue(genericRecord, addressId);
    }
}
