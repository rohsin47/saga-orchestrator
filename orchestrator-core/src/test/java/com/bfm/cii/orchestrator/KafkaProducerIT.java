package com.bfm.cii.orchestrator;

import java.math.BigDecimal;
import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.StringSerializer;

import com.bfm.cii.orchestrator.domain.Instruction;
import com.bfm.cii.orchestrator.domain.InstructionState;
import com.bfm.cii.orchestrator.events.InstructionCreatedEvent;
import com.bfm.cii.orchestrator.events.InstructionEventInfo;
import com.bfm.cii.orchestrator.kafka.serde.InstructionEventInfoSerde;
import com.bfm.cii.orchestrator.kafka.topic.TopicManager;
import com.bfm.lib.comstp.api.bms.instruction.TransactionType;

/**
 * @author rohsingh
 *
 */
public class KafkaProducerIT {

    // broker-topic-partition
    public static final String KAFKA_ZOOKEEPER = "ZK_LOCAL_SHARED";
    public static final String KAFKA_CLUSTER = "localhost:9092";
    public static final String KAFKA_TOPIC = TopicManager.ORDER_REPLIES.name();

    // producer
    public static final String KAFKA_PRODUCER_RETRIES = "0";
    public static final String KAFKA_PRODUCER_BATCH_SIZE = "16384";
    public static final String KAFKA_PRODUCER_LINGER_MS = "1";
    public static final String KAFKA_PRODUCER_BUFFER_MEMORY = "33554432";
    public static final String KAFKA_PRODUCER_ACKS = "all";
    public static final String KAFKA_PRODUCER_KEY_SERIALIZER = "org.apache.kafka.common.serialization.StringSerializer";
    public static final String KAFKA_PRODUCER_VALUE_SERIALIZER = "org.apache.kafka.common.serialization.StringSerializer";
    public static final String KAFKA_PRODUCER_COMPRESSION_TYPE = "lz4";

    public static Properties defaultProducerProperties() {
        Properties props = new Properties();
        props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_CLUSTER);
        props.setProperty(ProducerConfig.ACKS_CONFIG, KAFKA_PRODUCER_ACKS);
        props.setProperty(ProducerConfig.RETRIES_CONFIG, KAFKA_PRODUCER_RETRIES);
        return props;
    }

    public static void main(String[] args) {

        Instruction ord = Instruction.InstructionBuilder.newBuilder()
                .withOrderId(UUID.randomUUID().toString())
                .withOrderState(InstructionState.INSTRUCTION_CREATED)
                .withTxnType(TransactionType.BUY)
                .withPortfolioCode(34567)
                .withAsset("BRSTEST")
                .withAmount(BigDecimal.valueOf(100))
                .build();
        
        Serde<InstructionEventInfo> infoSerde = new InstructionEventInfoSerde();
        final KafkaProducer<String, InstructionEventInfo> producer = new KafkaProducer<>(defaultProducerProperties(), new StringSerializer(), infoSerde.serializer());
        
        InstructionCreatedEvent createEvent = new InstructionCreatedEvent();
        createEvent.setOrder(ord);
        createEvent.setOrderId(ord.getInstructionId());
        InstructionEventInfo eventInfo = new InstructionEventInfo(createEvent);
        
        final ProducerRecord<String, InstructionEventInfo> record = new ProducerRecord<>(KAFKA_TOPIC, createEvent.getOrderId(), eventInfo);
      
        producer.send(record);

        System.out.println("Sent");

        infoSerde.close();
        producer.close();
    }

}
