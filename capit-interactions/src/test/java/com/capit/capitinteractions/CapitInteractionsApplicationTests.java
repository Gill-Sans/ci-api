package com.capit.capitinteractions;

import com.capit.capitinteractions.config.Kafka.KafkaConsumerConfig;
import com.capit.capitinteractions.config.Kafka.KafkaProducerConfig;
import com.capit.capitinteractions.config.Kafka.KafkaTopicConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class CapitInteractionsApplicationTests {

	@MockitoBean
	private KafkaConsumerConfig kafkaConsumerConfig;
	@MockitoBean
	private KafkaProducerConfig kafkaProducerConfig;
	@MockitoBean
	private KafkaTopicConfig kafkaTopicConfig;

	@Test
	void contextLoads() {
	}

}
