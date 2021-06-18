/* Copyright (C) 2020 BlackRock. Created on Jun 16, 2020 */
package com.bfm.cii.create;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomUtils;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.stereotype.Component;

import com.bfm.cii.posting.FundOrderProto.FundOrder;
import com.bfm.cii.posting.NewCashProto;
import com.bfm.cii.posting.PartitionKeyProto;
import com.bfm.cii.posting.command.CiiCreationCommandProto;
import com.bfm.cii.posting.command.CiiCreationCommandProto.CiiCreationCommand;
import com.bfm.cii.posting.command.CiiValidationCommandProto;
import com.bfm.cii.posting.command.CiiValidationCommandProto.CiiValidationCommand;
import com.bfm.cii.posting.command.GenericCommandProto;
import com.bfm.cii.posting.command.SettlementCreationCommandProto;
import com.bfm.cii.posting.command.SettlementCreationCommandProto.SettlementCreationCommand;
import com.bfm.cii.posting.event.CiiCreatedEventProto;
import com.bfm.cii.posting.event.CiiCreatedEventProto.CiiCreatedEvent;
import com.bfm.cii.posting.event.CiiValidatedEventProto;
import com.bfm.cii.posting.event.EventTypeProto.EventType;
import com.bfm.cii.posting.event.GenericEventProto;
import com.bfm.cii.posting.event.SettlementCreatedEventProto;
import com.bfm.cii.posting.event.SettlementCreatedEventProto.SettlementCreatedEvent;
import com.bfm.cii.posting.event.CiiValidatedEventProto.CiiValidatedEvent;
import com.bfm.cmx.kafka.topic.Topic;
import com.bfm.cmx.kafka.topic.TopicConstants;
import com.bfm.cmx.kafka.topic.TopicManager;
import com.bfm.cmx.kafka.util.KafkaPropertiesBuilder;
import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;

/**
 * @author rohsingh
 *
 */
@Component
public class TransactionKafkaTopologyBuilder {

	public void startTopology(final KafkaStreams kafkaStreams) {
		// start processing
		kafkaStreams.start();
		// Add shutdown hook to respond to SIGTERM and gracefully close Kafka Streams
		Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
	}

	public KafkaStreams buildCiiCreationStream(final String applicationId) {
		// stream configuration
		final Properties streamsConfiguration = KafkaPropertiesBuilder.newBuilder()
			.withDefaultBootstrapServers()
			.withDefaultStreamProps()
			.withProperty(StreamsConfig.APPLICATION_ID_CONFIG, "transaction-service")
			.build();
		// stream builder
		final StreamsBuilder builder = new StreamsBuilder();

		final TopicManager topicManager = TopicManager.newTopicManager();
		Topic<PartitionKeyProto.PartitionKey, GenericCommandProto.GenericCommand> topic = topicManager.getCiiDbCreationCommandTopic();
		Topic<PartitionKeyProto.PartitionKey, GenericEventProto.GenericEvent> topic2 = topicManager.getInstructionProcessingEventTopic();
		// consume events
		builder.stream(topic.getName(), Consumed.with(topic.getKeySerde(), topic.getValueSerde())).map((key, val) -> {
			CiiCreationCommand cmd = null;
			try {
				cmd = val.getCommandDetail().unpack(CiiCreationCommand.class);
			} catch (InvalidProtocolBufferException e) {
				e.printStackTrace();
			}
			GenericEventProto.GenericEvent eventInfo = GenericEventProto.GenericEvent.newBuilder()
				.setEventType(EventType.CII_CREATED_EVENT)
				.setEventDetail(Any.pack(CiiCreatedEventProto.CiiCreatedEvent.newBuilder()
					.setCiiId(RandomUtils.nextLong(4567, 6578))
					.setSenderRef(cmd.getFundOrder().getSenderRef())
					.setPartitionKey(key)
					.build()))
				.build();
			return KeyValue.pair(key, eventInfo);
		}).peek((k, v) -> {
			try {
				TimeUnit.SECONDS.sleep(3);
				CiiCreatedEvent cii = v.getEventDetail().unpack(CiiCreatedEvent.class);
				System.out.println("Sent event for cii : " + cii.getCiiId());
			} catch (InterruptedException | InvalidProtocolBufferException e) {
				e.printStackTrace();
			}
		}).to(topic2.getName(), Produced.with(topic2.getKeySerde(), topic2.getValueSerde()));

		// start streaming events
		return new KafkaStreams(builder.build(), streamsConfiguration);
	}

	public KafkaStreams buildCiiValidationStream(final String applicationId) {
		final Properties streamsConfiguration = KafkaPropertiesBuilder.newBuilder()
			.withDefaultBootstrapServers()
			.withDefaultStreamProps()
			.withProperty(StreamsConfig.APPLICATION_ID_CONFIG, "validation-service")
			.build();
		// stream builder
		final StreamsBuilder builder = new StreamsBuilder();

		final TopicManager topicManager = TopicManager.newTopicManager();
		Topic<PartitionKeyProto.PartitionKey, GenericCommandProto.GenericCommand> topic = topicManager.getCiiValidationCommandTopic();
		Topic<PartitionKeyProto.PartitionKey, GenericEventProto.GenericEvent> topic2 = topicManager.getInstructionProcessingEventTopic();
		// consume events
		builder.stream(topic.getName(), Consumed.with(topic.getKeySerde(), topic.getValueSerde())).filter((key, val) -> {
			try {
				return validateOrder(val.getCommandDetail().unpack(CiiValidationCommand.class).getFundOrder());
			} catch (InvalidProtocolBufferException e1) {
				e1.printStackTrace();
			}
			return false;
		}).map((key, val) -> {
			CiiValidationCommand cmd = null;
			try {
				cmd = val.getCommandDetail().unpack(CiiValidationCommand.class);
			} catch (InvalidProtocolBufferException e) {
				e.printStackTrace();
			}
			GenericEventProto.GenericEvent eventInfo = GenericEventProto.GenericEvent.newBuilder()
				.setEventType(EventType.CII_VALIDATED_EVENT)
				.setEventDetail(Any.pack(CiiValidatedEventProto.CiiValidatedEvent.newBuilder()
					.setCiiId(cmd.getCiiId())
					.setSenderRef(cmd.getFundOrder().getSenderRef())
					.setFundOrder(cmd.getFundOrder())
					.setPartitionKey(key)
					.build()))
				.build();
			return KeyValue.pair(key, eventInfo);
		}).peek((k, v) -> {
			try {
				TimeUnit.SECONDS.sleep(3);
				CiiValidatedEvent cii = v.getEventDetail().unpack(CiiValidatedEvent.class);
				System.out.println("Sent validated event for cii : " + cii.getCiiId());
			} catch (InterruptedException | InvalidProtocolBufferException e) {
				e.printStackTrace();
			}
		}).to(topic2.getName(), Produced.with(topic2.getKeySerde(), topic2.getValueSerde()));

		// start streaming events
		return new KafkaStreams(builder.build(), streamsConfiguration);
	}

	public KafkaStreams buildSettlementCreationStream(final String applicationId) {
		final Properties streamsConfiguration = KafkaPropertiesBuilder.newBuilder()
			.withDefaultBootstrapServers()
			.withDefaultStreamProps()
			.withProperty(StreamsConfig.APPLICATION_ID_CONFIG, "settlement-service")
			.build();
		// stream builder
		final StreamsBuilder builder = new StreamsBuilder();

		final TopicManager topicManager = TopicManager.newTopicManager();
		Topic<PartitionKeyProto.PartitionKey, GenericCommandProto.GenericCommand> topic = topicManager.getSettlementCreationCommandTopic();
		Topic<PartitionKeyProto.PartitionKey, GenericEventProto.GenericEvent> topic2 = topicManager.getInstructionProcessingEventTopic();
		// consume events
		builder.stream(topic.getName(), Consumed.with(topic.getKeySerde(), topic.getValueSerde())).map((key, val) -> {
			SettlementCreationCommand cmd = null;
			try {
				cmd = val.getCommandDetail().unpack(SettlementCreationCommand.class);
			} catch (InvalidProtocolBufferException e) {
				e.printStackTrace();
			}
			GenericEventProto.GenericEvent eventInfo = GenericEventProto.GenericEvent.newBuilder()
				.setEventType(EventType.SETTLEMENT_CREATED_EVENT)
				.setEventDetail(Any.pack(SettlementCreatedEventProto.SettlementCreatedEvent.newBuilder()
					.setCiiId(cmd.getCiiId())
					.setSenderRef(cmd.getFundOrder().getSenderRef())
					.setPartitionKey(key)
					.setNewcash(NewCashProto.NewCash.newBuilder().setUniqueKey(345679).build())
					.build()))
				.build();
			return KeyValue.pair(key, eventInfo);
		}).peek((k, v) -> {
			try {
				TimeUnit.SECONDS.sleep(3);
				SettlementCreatedEvent cii = v.getEventDetail().unpack(SettlementCreatedEvent.class);
				System.out.println("Sent settlement event for cii : " + cii.getCiiId());
			} catch (InterruptedException | InvalidProtocolBufferException e) {
				e.printStackTrace();
			}
		}).to(topic2.getName(), Produced.with(topic2.getKeySerde(), topic2.getValueSerde()));

		// start streaming events
		return new KafkaStreams(builder.build(), streamsConfiguration);
	}

	private boolean validateOrder(FundOrder ord) {
		return true;
	}

}
