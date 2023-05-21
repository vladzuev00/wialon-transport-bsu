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
        return new TransportableData(
                data.getId(),
                findEpochSeconds(data),
                findLatitudeDegrees(data),
                findLatitudeMinutes(data),
                findLatitudeMinuteShare(data),
                findLatitudeTypeValue(data),
                findLongitudeDegrees(data),
                findLongitudeMinutes(data),
                findLongitudeMinuteShare(data),
                findLongitudeTypeValue(data),
                data.getSpeed(),
                data.getCourse(),
                data.getAltitude(),
                data.getAmountOfSatellites(),
                data.getReductionPrecision(),
                data.getInputs(),
                data.getOutputs(),
                super.serializeAnalogInputs(data),
                data.getDriverKeyCode(),
                super.serializeParameters(data),
                findTrackerId(data)
        );
    }
}
