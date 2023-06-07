package by.bsu.wialontransport.kafka.producer;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.kafka.transportable.TransportableData;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public final class KafkaInboundDataProducer extends AbstractKafkaDataProducer<TransportableData> {

    public KafkaInboundDataProducer(@Qualifier("kafkaTemplateInboundData")
                                    final KafkaTemplate<Long, GenericRecord> kafkaTemplate,
                                    @Value("${kafka.topic.inbound-data.name}") final String topicName,
                                    @Qualifier("transportableDataSchema") final Schema schema) {
        super(kafkaTemplate, topicName, schema);
    }

    @Override
    protected TransportableData mapToTransportable(final Data data) {
        return TransportableData.builder()
                .epochSeconds(findEpochSeconds(data))
                .latitudeDegrees(findLatitudeDegrees(data))
                .latitudeMinutes(findLatitudeMinutes(data))
                .latitudeMinuteShare(findLatitudeMinuteShare(data))
                .latitudeTypeValue(findLatitudeTypeValue(data))
                .longitudeDegrees(findLongitudeDegrees(data))
                .longitudeMinutes(findLongitudeMinutes(data))
                .longitudeMinuteShare(findLongitudeMinuteShare(data))
                .longitudeTypeValue(findLongitudeTypeValue(data))
                .speed(data.getSpeed())
                .course(data.getCourse())
                .altitude(data.getAltitude())
                .amountOfSatellites(data.getAmountOfSatellites())
                .reductionPrecision(data.getReductionPrecision())
                .inputs(data.getInputs())
                .outputs(data.getOutputs())
                .serializedAnalogInputs(super.serializeAnalogInputs(data))
                .driverKeyCode(data.getDriverKeyCode())
                .serializedParameters(super.serializeParameters(data))
                .trackerId(findTrackerId(data))
                .build();
    }
}
