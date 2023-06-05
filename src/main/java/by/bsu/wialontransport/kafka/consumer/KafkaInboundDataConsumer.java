package by.bsu.wialontransport.kafka.consumer;

import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Data.Latitude;
import by.bsu.wialontransport.crud.dto.Data.Longitude;
import by.bsu.wialontransport.crud.service.AddressService;
import by.bsu.wialontransport.crud.service.DataService;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.kafka.consumer.exception.DataConsumingException;
import by.bsu.wialontransport.kafka.producer.KafkaSavedDataProducer;
import by.bsu.wialontransport.service.geocoding.GeocodingService;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static by.bsu.wialontransport.crud.dto.Data.createWithAddress;
import static java.lang.String.format;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;

@Component
public class KafkaInboundDataConsumer extends AbstractKafkaDataConsumer {
    private final DataService dataService;
    private final AddressService addressService;
    private final GeocodingService geocodingService;
    private final KafkaSavedDataProducer savedDataProducer;

    public KafkaInboundDataConsumer(final TrackerService trackerService,
                                    final DataService dataService,
                                    final AddressService addressService,
                                    @Qualifier("chainGeocodingService") final GeocodingService geocodingService,
                                    final KafkaSavedDataProducer savedDataProducer) {
        super(trackerService);
        this.dataService = dataService;
        this.addressService = addressService;
        this.geocodingService = geocodingService;
        this.savedDataProducer = savedDataProducer;
    }

    @Override
    @KafkaListener(
            topics = "${kafka.topic.inbound-data.name}",
            groupId = "${kafka.topic.inbound-data.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactoryInboundData"
    )
    public void consume(final List<ConsumerRecord<Long, GenericRecord>> consumerRecords) {
        super.consume(consumerRecords);
    }

    //TODO: refactor tests
    @Override
    protected Data mapToData(final GenericRecord genericRecord) {
        final LocalDateTime dateTime = extractDateTime(genericRecord);
        final Latitude latitude = super.extractLatitude(genericRecord);
        final Longitude longitude = super.extractLongitude(genericRecord);
        return Data.builder()
                .date(dateTime.toLocalDate())
                .time(dateTime.toLocalTime())
                .latitude(latitude)
                .longitude(longitude)
                .speed(extractSpeed(genericRecord))
                .course(extractCourse(genericRecord))
                .altitude(extractAltitude(genericRecord))
                .amountOfSatellites(extractAmountOfSatellites(genericRecord))
                .reductionPrecision(extractReductionPrecision(genericRecord))
                .inputs(extractInputs(genericRecord))
                .outputs(extractOutputs(genericRecord))
                .analogInputs(super.extractAnalogInputs(genericRecord))
                .driverKeyCode(extractDriverKeyCode(genericRecord))
                .parametersByNames(super.extractParametersByNames(genericRecord))
                .tracker(super.extractTracker(genericRecord))
                .address(this.findAddress(latitude, longitude))
                .build();
    }

    @Override
    @Transactional(isolation = READ_COMMITTED)
    protected void processData(final List<Data> data) {
        final List<Data> findDataWithSavedAddresses = this.findDataWithSavedAddresses(data);
        final List<Data> savedData = this.dataService.saveAll(findDataWithSavedAddresses);
        this.sendInSavedDataTopic(savedData);
    }

    private Address findAddress(final Latitude latitude, final Longitude longitude) {
        final Optional<Address> optionalAddress = this.geocodingService.receive(latitude, longitude);
        return optionalAddress.orElseThrow(
                () -> new DataConsumingException(
                        format(
                                "Impossible to find address by latitude='%s' and longitude='%s'",
                                latitude,
                                longitude
                        )
                )
        );
    }

    private void sendInSavedDataTopic(final List<Data> data) {
        data.forEach(this.savedDataProducer::send);
    }

    private List<Data> findDataWithSavedAddresses(final List<Data> source) {
        final Map<Address, List<Data>> dataGroupedByAddresses = source.stream()
                .collect(groupingBy(Data::getAddress, LinkedHashMap::new, toList()));
        dataGroupedByAddresses.replaceAll(this::mapToDataWithSavedAddressIfAddressIsNew);
        return dataGroupedByAddresses.values()
                .stream()
                .flatMap(Collection::stream)
                .toList();
    }

    /**
     * @param data    - data with given address
     * @param address - address of all data
     * @return data with saved address
     */
    private List<Data> mapToDataWithSavedAddressIfAddressIsNew(final Address address, final List<Data> data) {
        return isNewAddress(address) ? this.mapToDataWithSavedAddress(data, address) : data;
    }

    /**
     * @param source     - data with given address
     * @param newAddress - not saved address of all data
     * @return data with saved address
     */
    private List<Data> mapToDataWithSavedAddress(final List<Data> source, final Address newAddress) {
        final Address savedAddress = this.addressService.save(newAddress);
        return source.stream()
                .map(data -> createWithAddress(data, savedAddress))
                .toList();
    }

    private static boolean isNewAddress(final Address address) {
        return address.getId() == null;
    }
}
