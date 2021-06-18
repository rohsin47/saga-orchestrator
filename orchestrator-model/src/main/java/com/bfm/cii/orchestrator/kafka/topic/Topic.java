/*
* Copyright (C) 2020 BlackRock.
*
* Created on Jun 16, 2020
*
*/
package com.bfm.cii.orchestrator.kafka.topic;

import org.apache.kafka.common.serialization.Serde;

/**
 * @author rohsingh
 *
 */
public class Topic<K,V> {
    
    private final String name;
    private final Serde<K> keySerde;
    private final Serde<V> valueSerde;

    Topic(final String name, final Serde<K> keySerde, final Serde<V> valueSerde) {
      this.name = name;
      this.keySerde = keySerde;
      this.valueSerde = valueSerde;
      TopicManager.ALL.put(name, this);
    }

    public Serde<K> keySerde() {
      return keySerde;
    }

    public Serde<V> valueSerde() {
      return valueSerde;
    }

    public String name() {
      return name;
    }

    @Override
    public String toString() {
      return name;
    }
  }
