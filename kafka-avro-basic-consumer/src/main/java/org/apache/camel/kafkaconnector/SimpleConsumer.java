package org.apache.camel.kafkaconnector;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.avro.generic.GenericRecord;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.apicurio.registry.utils.serde.AvroKafkaDeserializer;

public class SimpleConsumer {

	public static void main(String[] args) throws JsonProcessingException {
		
		final Logger LOG = LoggerFactory.getLogger(SimpleConsumer.class);

		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("key.deserializer", StringDeserializer.class.getName());
		props.put("value.deserializer",AvroKafkaDeserializer.class.getName());
		props.put("apicurio.registry.url","http://localhost:8080/api");
		props.put("group.id", UUID.randomUUID().toString());
		props.put("auto.offset.reset", "earliest");
        props.put("apicurio.registry.global-id", "io.apicurio.registry.utils.serde.strategy.GetOrCreateIdStrategy");

		KafkaConsumer<String, GenericRecord> cons = new KafkaConsumer<String, GenericRecord>(props);
		List<String> topics = new ArrayList<String>();
		topics.add(args[0]);
		cons.subscribe(topics);


			  while (true) {
			    ConsumerRecords<String, GenericRecord> consumerRecords = cons.poll(Duration.ofMillis(1000L));
			    if (consumerRecords.count() > 0) {
			    	for (Iterator iterator = consumerRecords.iterator(); iterator.hasNext();) {
			    		ConsumerRecord<String, GenericRecord> rec = (ConsumerRecord) iterator.next();
			    		LOG.info("Text is " + rec.value().get("text").toString());
			    		LOG.info("Username is " + rec.value().get("username").toString());
					}
			    }
			  }
	}
}
