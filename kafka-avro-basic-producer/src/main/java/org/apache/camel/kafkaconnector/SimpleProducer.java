/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.kafkaconnector;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.avro.Schema;
import org.apache.avro.Schema.Parser;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.camel.component.slack.helper.SlackMessage;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import io.apicurio.registry.utils.serde.AvroKafkaSerializer;

public class SimpleProducer {

    public static void main(String[] args) throws IOException {

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", StringSerializer.class.getName());
        props.put("value.serializer", AvroKafkaSerializer.class.getName());
        props.put("value.converter.schemas.enable", true);
        props.put("apicurio.registry.url", "http://localhost:8080/api/");
        props.put("apicurio.registry.global-id", "io.apicurio.registry.utils.serde.strategy.GetOrCreateIdStrategy");

        KafkaProducer<String, GenericRecord> prod = new KafkaProducer<String, GenericRecord>(props);
        
        Schema parse = new Parser().parse(new File("src/main/resources/slackmessage.asvc"));
        
        GenericData.Record record = new GenericData.Record(parse);
        record.put("text", "Hello");
        record.put("username", "camel");
        
       
        ProducerRecord<String, GenericRecord> rec = new ProducerRecord<String, GenericRecord>(args[0], "1", record);

        prod.send(rec);

        prod.close();
    }
}
