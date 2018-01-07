package com.sai.spring.kafka.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

public class Producer {
	
	 private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);

	  @Autowired
	  private KafkaTemplate<String, String> kafkaTemplate;

	  public void send(String topic, String payload) {
	    LOGGER.info("sending payload='{}' to topic='{}'", payload, topic);
	    kafkaTemplate.send(topic, payload);
	  }

}
