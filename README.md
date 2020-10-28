## Basic Kafka Producer Consumer example with avro

You'll need apicurio schema registry running

docker run -it -p 8080:8080 apicurio/apicurio-registry-mem:1.3.1.Final

To run the producer:

mvn clean compile exec:exec -Dkafka.topic.name=mytopic

To run the consumer:

mvn clean compile exec:exec -Dkafka.topic.name=mytopic

the default topic name is mytopic
