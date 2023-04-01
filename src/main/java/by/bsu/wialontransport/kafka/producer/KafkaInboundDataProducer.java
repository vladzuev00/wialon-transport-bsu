package by.bsu.wialontransport.kafka.producer;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public final class KafkaInboundDataProducer extends AbstractKafkaDataProducer {

    public KafkaInboundDataProducer(@Qualifier("kafkaTemplateInboundData")
                                    final KafkaTemplate<Long, GenericRecord> kafkaTemplate,
                                    @Value("${kafka.topic.inbound-data.name}") final String topicName,
                                    @Qualifier("transportableDataSchema") final Schema schema) {
        super(kafkaTemplate, topicName, schema);
    }

}
