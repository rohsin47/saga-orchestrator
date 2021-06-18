package com.bfm.cii.create;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.kafka.streams.KafkaStreams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author rohsingh
 *
 */
@Component
public class TransactionKafkaTopologyProcessor {

	@Autowired
	TransactionKafkaTopologyBuilder ciiKafkaTopologyBuilder;

	List<KafkaStreams> streams;

	@PostConstruct
	public void processToplogy() {
		streams = new ArrayList<>();
		//streams.add(ciiKafkaTopologyBuilder.buildCiiCreationStream("cii-creations"));
		streams.add(ciiKafkaTopologyBuilder.buildCiiValidationStream("cii-validations"));
		//streams.add(ciiKafkaTopologyBuilder.buildSettlementCreationStream("cii-settlements"));
		streams.forEach(ciiKafkaTopologyBuilder::startTopology);
	}

}
