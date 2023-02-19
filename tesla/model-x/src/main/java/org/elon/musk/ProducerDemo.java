package org.elon.musk;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemo {

    private static final Logger log = LoggerFactory.getLogger(ProducerDemo.class.getSimpleName());
    public static void main(String[] args) {
        log.info("Hello Elon Welcome  to Producer Class");
        // STEP 1 : Create Producer Properties

        Properties properties =  new Properties();

        //Connect to locqlhost
        // properties.setProperty("bootstrap.servers","127.0.0.1:9092");

        //Connect to Conduktor
        properties.setProperty("bootstrap.servers","cluster.playground.cdkt.io:9092");
        properties.setProperty("security.protocol","SASL_SSL");
        properties.setProperty("sasl.jaas.config","org.apache.kafka.common.security.plain.PlainLoginModule required username=\"5YddWNqJ2UUYtYmly344I5\" password=\"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL2F1dGguY29uZHVrdG9yLmlvIiwic291cmNlQXBwbGljYXRpb24iOiJhZG1pbiIsInVzZXJNYWlsIjpudWxsLCJwYXlsb2FkIjp7InZhbGlkRm9yVXNlcm5hbWUiOiI1WWRkV05xSjJVVVl0WW1seTM0NEk1Iiwib3JnYW5pemF0aW9uSWQiOjY5MDM0LCJ1c2VySWQiOjc5Njk0LCJmb3JFeHBpcmF0aW9uQ2hlY2siOiJlODBjMmMxMy1kMWU2LTRhMGQtODAwYy0xODczOTdlNTI3MDUifX0.9xwdQ5l1hShCGOJRVvn4o5z6tT6-xZw3S8Q3b7agtrI\";");
        properties.setProperty("sasl.mechanism","PLAIN");

        //set producer properties
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());

        //Step 2 : Create Producer
        KafkaProducer<String,String> producer =  new KafkaProducer<>(properties);

        //create ProducerRecord
        ProducerRecord<String,String> producerRecord = new ProducerRecord<>("demo_java","helloWorld");
        producer.send(producerRecord);
        producer.flush();
        producer.close();








    }
}
