package com.sai.spring.kafka.SpringKafka;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.context.junit4.SpringRunner;

import com.sai.spring.kafka.consumer.Receiver;
import com.sai.spring.kafka.producer.Producer;


@RunWith(SpringRunner.class)
@SpringBootTest
public class Test {

	protected final static String HELLOWORLD_TOPIC = "helloworld.t";

	@Autowired
	private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

	@Autowired
	private Receiver receiver;

	@Autowired
	private Producer producer;

	@ClassRule
	public static KafkaEmbedded kafkaEmbedded = new KafkaEmbedded(1, true, HELLOWORLD_TOPIC);

	@Before
	public void runBeforeTestMethod() throws Exception {
		// wait until all the partitions are assigned
		for (MessageListenerContainer messageListenerContainer : kafkaListenerEndpointRegistry
				.getListenerContainers()) {
			ContainerTestUtils.waitForAssignment(messageListenerContainer, kafkaEmbedded.getPartitionsPerTopic());
		}
	}
	
	
	public void testReceive() throws Exception {
		producer.send(HELLOWORLD_TOPIC, "Hello Spring Kafka!");

		receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
		assertThat((receiver).getLatch().getCount()).isEqualTo(0);
	}
}
