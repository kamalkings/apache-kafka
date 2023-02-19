package org.elon.musk;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.CooperativeStickyAssignor;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerDemoCooperative {

    private static final Logger log = LoggerFactory.getLogger(ConsumerDemoCooperative.class.getSimpleName());
    public static void main(String[] args) {
        log.info("Hello Elon Welcome  to Consumer Class");

        String groupId = "my-java-application";
        String topic = "demo_java";
        // STEP 1 : Create Producer Properties
        Properties properties =  new Properties();

        //Connect to locqlhost
        // properties.setProperty("bootstrap.servers","127.0.0.1:9092");

        //Connect to Conduktor
        properties.setProperty("bootstrap.servers","cluster.playground.cdkt.io:9092");
        properties.setProperty("security.protocol","SASL_SSL");
        properties.setProperty("sasl.jaas.config","org.apache.kafka.common.security.plain.PlainLoginModule required username=\"5YddWNqJ2UUYtYmly344I5\" password=\"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL2F1dGguY29uZHVrdG9yLmlvIiwic291cmNlQXBwbGljYXRpb24iOiJhZG1pbiIsInVzZXJNYWlsIjpudWxsLCJwYXlsb2FkIjp7InZhbGlkRm9yVXNlcm5hbWUiOiI1WWRkV05xSjJVVVl0WW1seTM0NEk1Iiwib3JnYW5pemF0aW9uSWQiOjY5MDM0LCJ1c2VySWQiOjc5Njk0LCJmb3JFeHBpcmF0aW9uQ2hlY2siOiJlODBjMmMxMy1kMWU2LTRhMGQtODAwYy0xODczOTdlNTI3MDUifX0.9xwdQ5l1hShCGOJRVvn4o5z6tT6-xZw3S8Q3b7agtrI\";");
        properties.setProperty("sasl.mechanism","PLAIN");

        //set consumer properties
        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer", StringDeserializer.class.getName());
        properties.setProperty("group.id",groupId);
        properties.setProperty("auto.offset.reset","earliest");
        properties.setProperty("partition.assignment.strategy", CooperativeStickyAssignor.class.getName());

        //Create Consumer
        KafkaConsumer<String,String> consumer = new KafkaConsumer<>(properties);

        //subscribe to a topic
        consumer.subscribe(Arrays.asList(topic));

        //get Reference to MainThread
        final Thread mainThread = Thread.currentThread();

        //adding Shutdown Hook
        Runtime.getRuntime().addShutdownHook(new Thread(){
            public void run(){
                log.info("Detected  a ShutDown, Lets call condumer.WakeUp");
                consumer.wakeup();

                try {
                    mainThread.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        });

        try{
            //poll for data
            while(true){

                ConsumerRecords<String,String> consumerRecords =
                        consumer.poll(Duration.ofMillis(1000));
                for(ConsumerRecord<String,String> records : consumerRecords){
                    log.info("Key : " + records.key() + "Value : " + records.value());
                    log.info("Partition : " + records.partition() + "Offset : " + records.offset());
                }

            }
        }
        catch (WakeupException e){
            log.info("Consumer is Staring to Shutdown");
        }
        catch (Exception e){
            log.error("Unexcepted Exception",e);
        }
        finally {
            consumer.close();
            log.info("Consumer Shout down Gracefullyy in Finally");
        }






    }
}
