package com.capit.capitinteractions.config;

import com.capit.capitinteractions.domain.checkin.events.CheckinKafkaEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;
import reactor.core.publisher.Sinks;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Configuration
public class ReactorKafkaConfig {

    private final KafkaProperties kafkaProperties;

    public ReactorKafkaConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @Bean
    public String instanceId() {
        return UUID.randomUUID().toString();
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String,Object> adminProps = kafkaProperties.buildAdminProperties(null);
        return new KafkaAdmin(adminProps);
    }

    @Bean
    public NewTopic checkinEventsTopic(
            @Value("${interactions.topic.checkin.name:checkin-events}") String topicName,
            @Value("${interactions.topic.checkin.partitions:1}") int partitions,
            @Value("${interactions.topic.checkin.replicas:1}") short replicas
    ) {
        return new NewTopic(topicName, partitions, replicas);
    }

    @Bean
    public SenderOptions<String, CheckinKafkaEvent> senderOptions() {
        Map<String,Object> props = kafkaProperties.buildProducerProperties();
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,   StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return SenderOptions.create(props);
    }

    @Bean
    public KafkaSender<String, CheckinKafkaEvent> kafkaSender(
            SenderOptions<String, CheckinKafkaEvent> senderOptions
    ) {
        return KafkaSender.create(senderOptions);
    }

    @Bean
    public ReceiverOptions<String, CheckinKafkaEvent> receiverOptions(
            String instanceId,
            @Value("${interactions.topic.checkin.name:checkin-events}") String topicName
    ) {
        Map<String,Object> props = kafkaProperties.buildConsumerProperties();
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "checkin-" + instanceId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.capit.capitinteractions.domain.checkin.events");

        return ReceiverOptions.<String, CheckinKafkaEvent>create(props)
                .subscription(List.of(topicName));
    }

    @Bean
    public KafkaReceiver<String, CheckinKafkaEvent> kafkaReceiver(
            ReceiverOptions<String, CheckinKafkaEvent> receiverOptions
    ) {
        return KafkaReceiver.create(receiverOptions);
    }

    /** Now raw CheckinKafkaEvent and bounded buffer to avoid unbounded backlog **/
    @Bean
    public Sinks.Many<CheckinKafkaEvent> checkinSink() {
        return Sinks.many().multicast().onBackpressureBuffer();
    }
}
