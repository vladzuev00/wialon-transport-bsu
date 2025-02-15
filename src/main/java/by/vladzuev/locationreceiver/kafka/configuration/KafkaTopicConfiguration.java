package by.vladzuev.locationreceiver.kafka.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfiguration {

    @Bean
    public NewTopic inboundDataTopic(
            @Value("${kafka.topic.inbound-data.name}") final String topicName,
            @Value("${kafka.topic.inbound-data.partitions-amount}") final int partitionsAmount,
            @Value("${kafka.topic.inbound-data.replication-factor}") final short replicationFactor
    ) {
        return new NewTopic(topicName, partitionsAmount, replicationFactor);
    }

    @Bean
    public NewTopic savedDataTopic(
            @Value("${kafka.topic.saved-data.name}") final String topicName,
            @Value("${kafka.topic.saved-data.partitions-amount}") final int partitionsAmount,
            @Value("${kafka.topic.saved-data.replication-factor}") final short replicationFactor
    ) {
        return new NewTopic(topicName, partitionsAmount, replicationFactor);
    }
}
