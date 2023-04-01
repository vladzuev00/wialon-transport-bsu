package by.bsu.wialontransport.kafka.consumer;

import org.apache.avro.generic.GenericRecord;

public abstract class AbstractKafkaGenericRecordConsumer<K, DATA>
        extends AbstractKafkaConsumer<K, GenericRecord, DATA> {

}
