package by.bsu.wialontransport.kafka.producer.data;

import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.kafka.producer.data.view.InboundParameterView;
import by.bsu.wialontransport.kafka.transportable.data.TransportableInboundData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public final class KafkaInboundDataProducer extends KafkaDataProducer {

    public KafkaInboundDataProducer(@Qualifier("kafkaTemplateInboundData") final KafkaTemplate<Long, GenericRecord> kafkaTemplate,
                                    @Value("${kafka.topic.inbound-data.name}") final String topicName,
                                    @Qualifier("transportableDataSchema") final Schema schema,
                                    final ObjectMapper objectMapper) {
        super(kafkaTemplate, topicName, schema, objectMapper);
    }

    @Override
    protected TransportableInboundData createTransportable(final CreatingTransportableContext context) {
        return TransportableInboundData.builder()
                .epochSeconds(context.getEpochSeconds())
                .latitude(context.getLatitude())
                .longitude(context.getLongitude())
                .course(context.getCourse())
                .speed(context.getSpeed())
                .altitude(context.getAltitude())
                .amountOfSatellites(context.getAmountOfSatellites())
                .reductionPrecision(context.getReductionPrecision())
                .inputs(context.getInputs())
                .outputs(context.getOutputs())
                .serializedAnalogInputs(context.getSerializedAnalogInputs())
                .driverKeyCode(context.getDriverKeyCode())
                .serializedParameters(context.getSerializedParameters())
                .trackerId(context.getTrackerId())
                .build();
    }

    @Override
    protected InboundParameterView createParameterView(final Parameter parameter) {
        return InboundParameterView.builder()
                .name(parameter.getName())
                .type(parameter.getType())
                .value(parameter.getValue())
                .build();
    }
}
